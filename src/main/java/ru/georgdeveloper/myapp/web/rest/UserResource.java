package ru.georgdeveloper.myapp.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.georgdeveloper.myapp.config.Constants;
import ru.georgdeveloper.myapp.domain.User;
import ru.georgdeveloper.myapp.repository.UserRepository;
import ru.georgdeveloper.myapp.security.AuthoritiesConstants;
import ru.georgdeveloper.myapp.service.MailService;
import ru.georgdeveloper.myapp.service.UserService;
import ru.georgdeveloper.myapp.service.dto.AdminUserDTO;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import ru.georgdeveloper.myapp.web.rest.errors.EmailAlreadyUsedException;
import ru.georgdeveloper.myapp.web.rest.errors.LoginAlreadyUsedException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST контроллер для управления пользователями.
 * <p>
 * Этот класс работает с сущностью {@link ru.georgdeveloper.myapp.domain.User} и получает связанные с ней authorities (роли).
 * <p>
 * В обычном сценарии лучше было бы использовать eager-связь между User и Authority,
 * чтобы отправлять все данные клиенту сразу: это позволило бы обойтись без View Model и DTO,
 * уменьшить объем кода и использовать outer join для лучшей производительности.
 * <p>
 * Однако здесь используются View Model и DTO по трем причинам:
 * <ul>
 * <li>Мы хотим сохранить lazy-ассоциацию между пользователем и authorities, потому что
 * часто будут использоваться связи с пользователем, и мы не хотим каждый раз загружать authorities
 * без необходимости (для оптимизации производительности). Это главная цель: мы не должны
 * влиять на производительность приложения из-за этого сценария.</li>
 * <li>Отсутствие outer join приводит к n+1 запросам к базе данных. Это не является проблемой,
 * так как у нас по умолчанию есть кеш второго уровня. Это означает, что при первом HTTP-вызове
 * мы делаем n+1 запросов, но затем все authorities берутся из кеша, что на практике
 * работает лучше, чем outer join (который получает много данных из БД при каждом HTTP-вызове).</li>
 * <li>Поскольку это управление пользователями, из соображений безопасности лучше иметь слой DTO.</li>
 * </ul>
 * <p>
 * Альтернативой могло бы быть использование специального JPA entity graph для этого случая.
 */
@RestController
@RequestMapping("/api/admin")
public class UserResource {

    // Список разрешенных свойств для сортировки
    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList(
            "id",
            "login",
            "firstName",
            "lastName",
            "email",
            "activated",
            "langKey",
            "createdBy",
            "createdDate",
            "lastModifiedBy",
            "lastModifiedDate"
        )
    );

    // Логгер для записи информации о работе контроллера
    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    // Название клиентского приложения из конфигурации
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    // Сервис для работы с пользователями
    private final UserService userService;

    // Репозиторий для работы с данными пользователей
    private final UserRepository userRepository;

    // Сервис для отправки email
    private final MailService mailService;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param userService сервис пользователей
     * @param userRepository репозиторий пользователей
     * @param mailService сервис отправки email
     */
    public UserResource(UserService userService, UserRepository userRepository, MailService mailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /admin/users}  : Создает нового пользователя.
     * <p>
     * Создает нового пользователя, если логин и email еще не используются, и отправляет
     * письмо со ссылкой для активации.
     * Пользователь должен быть активирован при создании.
     *
     * @param userDTO данные пользователя для создания.
     * @return ответ со статусом {@code 201 (Created)} и телом нового пользователя,
     *         или статус {@code 400 (Bad Request)} если логин или email уже используются.
     * @throws URISyntaxException если синтаксис URI некорректен.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} если логин или email уже используются.
     */
    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody AdminUserDTO userDTO) throws URISyntaxException {
        LOG.debug("REST запрос для сохранения User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("Новый пользователь не может иметь ID", "userManagement", "idexists");
            // Приводим логин к нижнему регистру перед сравнением с базой данных
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/admin/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * {@code PUT /admin/users} : Обновляет существующего пользователя.
     *
     * @param login логин пользователя для обновления (опциональный параметр)
     * @param userDTO новые данные пользователя.
     * @return ответ со статусом {@code 200 (OK)} и телом обновленного пользователя.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} если email уже используется.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} если логин уже используется.
     */
    @PutMapping({ "/users", "/users/{login}" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<AdminUserDTO> updateUser(
        @PathVariable(name = "login", required = false) @Pattern(regexp = Constants.LOGIN_REGEX) String login,
        @Valid @RequestBody AdminUserDTO userDTO
    ) {
        LOG.debug("REST запрос для обновления User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.orElseThrow().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.orElseThrow().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<AdminUserDTO> updatedUser = userService.updateUser(userDTO);

        return ResponseUtil.wrapOrNotFound(
            updatedUser,
            HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.getLogin())
        );
    }

    /**
     * {@code GET /admin/users} : Получает всех пользователей со всеми деталями - доступно только администраторам.
     *
     * @param pageable информация о пагинации и сортировке.
     * @return ответ со статусом {@code 200 (OK)} и списком всех пользователей.
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST запрос для получения всех User для администратора");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<AdminUserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Проверяет, что сортировка выполняется только по разрешенным свойствам.
     *
     * @param pageable информация о пагинации и сортировке
     * @return true если все свойства для сортировки разрешены, false в противном случае
     */
    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * {@code GET /admin/users/:login} : Получает пользователя по логину.
     *
     * @param login логин пользователя для поиска.
     * @return ответ со статусом {@code 200 (OK)} и телом пользователя,
     *         или статус {@code 404 (Not Found)} если пользователь не найден.
     */
    @GetMapping("/users/{login}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<AdminUserDTO> getUser(@PathVariable("login") @Pattern(regexp = Constants.LOGIN_REGEX) String login) {
        LOG.debug("REST запрос для получения User : {}", login);
        return ResponseUtil.wrapOrNotFound(userService.getUserWithAuthoritiesByLogin(login).map(AdminUserDTO::new));
    }

    /**
     * {@code DELETE /admin/users/:login} : Удаляет пользователя по логину.
     *
     * @param login логин пользователя для удаления.
     * @return ответ со статусом {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{login}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable("login") @Pattern(regexp = Constants.LOGIN_REGEX) String login) {
        LOG.debug("REST запрос для удаления User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "userManagement.deleted", login)).build();
    }
}
