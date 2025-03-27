package ru.georgdeveloper.myapp.web.rest;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.georgdeveloper.myapp.service.UserService;
import ru.georgdeveloper.myapp.service.dto.UserDTO;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST контроллер для работы с публичной информацией о пользователях.
 * Доступен всем пользователям без аутентификации.
 */
@RestController
@RequestMapping("/api")
public class PublicUserResource {

    // Список разрешенных свойств для сортировки
    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "login", "firstName", "lastName", "email", "activated", "langKey")
    );

    private static final Logger LOG = LoggerFactory.getLogger(PublicUserResource.class);

    private final UserService userService;

    /**
     * Конструктор контроллера.
     *
     * @param userService сервис для работы с пользователями
     */
    public PublicUserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получает список всех пользователей с публичной информацией.
     * GET /api/users
     *
     * @param pageable параметры пагинации и сортировки
     * @return ResponseEntity со списком пользователей и заголовками пагинации
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllPublicUsers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST запрос на получение публичной информации о пользователях");

        // Проверка разрешенных полей для сортировки
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<UserDTO> page = userService.getAllPublicUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Проверяет, что сортировка запрошена только по разрешенным полям.
     *
     * @param pageable параметры пагинации
     * @return true если все поля сортировки разрешены, false в противном случае
     */
    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }
}
