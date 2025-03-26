package ru.georgdeveloper.myapp.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.config.Constants;
import ru.georgdeveloper.myapp.domain.Authority;
import ru.georgdeveloper.myapp.domain.User;
import ru.georgdeveloper.myapp.repository.AuthorityRepository;
import ru.georgdeveloper.myapp.repository.UserRepository;
import ru.georgdeveloper.myapp.security.AuthoritiesConstants;
import ru.georgdeveloper.myapp.security.SecurityUtils;
import ru.georgdeveloper.myapp.service.dto.AdminUserDTO;
import ru.georgdeveloper.myapp.service.dto.UserDTO;
import tech.jhipster.security.RandomUtil;

/**
 * Сервисный класс для управления пользователями.
 */
@Service
@Transactional
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    /**
     * Конструктор сервиса пользователей.
     *
     * @param userRepository репозиторий пользователей
     * @param passwordEncoder кодировщик паролей
     * @param authorityRepository репозиторий ролей
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    /**
     * Активирует регистрацию пользователя по ключу активации.
     *
     * @param key ключ активации
     * @return пользователь, если активация прошла успешно
     */
    public Optional<User> activateRegistration(String key) {
        LOG.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                LOG.debug("Activated user: {}", user);
                return user;
            });
    }

    /**
     * Завершает сброс пароля пользователя.
     *
     * @param newPassword новый пароль
     * @param key ключ сброса
     * @return пользователь, если сброс прошел успешно
     */
    public Optional<User> completePasswordReset(String newPassword, String key) {
        LOG.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                return user;
            });
    }

    /**
     * Запрашивает сброс пароля для пользователя с указанной почтой.
     *
     * @param mail электронная почта пользователя
     * @return пользователь, если запрос прошел успешно
     */
    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                return user;
            });
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param userDTO данные пользователя
     * @param password пароль пользователя
     * @return зарегистрированный пользователь
     * @throws UsernameAlreadyUsedException если логин уже используется
     * @throws EmailAlreadyUsedException если email уже используется
     */
    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new EmailAlreadyUsedException();
                }
            });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        LOG.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    /**
     * Удаляет неактивированного пользователя.
     *
     * @param existingUser существующий пользователь
     * @return true, если пользователь был удален
     */
    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }

    /**
     * Создает нового пользователя.
     *
     * @param userDTO данные пользователя
     * @return созданный пользователь
     */
    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        LOG.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Обновляет всю информацию о пользователе.
     *
     * @param userDTO данные пользователя для обновления
     * @return обновленный пользователь
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional.of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO
                    .getAuthorities()
                    .stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                userRepository.save(user);
                LOG.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(AdminUserDTO::new);
    }

    /**
     * Удаляет пользователя по логину.
     *
     * @param login логин пользователя
     */
    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                userRepository.delete(user);
                LOG.debug("Deleted User: {}", user);
            });
    }

    /**
     * Обновляет базовую информацию о текущем пользователе.
     *
     * @param firstName имя
     * @param lastName фамилия
     * @param email электронная почта
     * @param langKey языковой ключ
     * @param imageUrl URL изображения
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                userRepository.save(user);
                LOG.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Изменяет пароль текущего пользователя.
     *
     * @param currentClearTextPassword текущий пароль в открытом виде
     * @param newPassword новый пароль
     * @throws InvalidPasswordException если текущий пароль неверен
     */
    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                LOG.debug("Changed password for User: {}", user);
            });
    }

    /**
     * Получает всех управляемых пользователей с пагинацией.
     *
     * @param pageable параметры пагинации
     * @return страница с пользователями
     */
    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    /**
     * Получает всех публичных пользователей с пагинацией.
     *
     * @param pageable параметры пагинации
     * @return страница с пользователями
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    /**
     * Получает пользователя с ролями по логину.
     *
     * @param login логин пользователя
     * @return пользователь с ролями
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    /**
     * Получает текущего аутентифицированного пользователя с ролями.
     *
     * @return текущий пользователь с ролями
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Удаляет неактивированных пользователей (активируется ежедневно в 01:00).
     * Неактивированные пользователи удаляются через 3 дня после создания.
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                LOG.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
            });
    }

    /**
     * Получает список всех доступных ролей.
     *
     * @return список названий ролей
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).toList();
    }
}
