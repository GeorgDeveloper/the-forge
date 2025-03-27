package ru.georgdeveloper.myapp.web.rest;

import jakarta.validation.Valid;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.georgdeveloper.myapp.domain.User;
import ru.georgdeveloper.myapp.repository.UserRepository;
import ru.georgdeveloper.myapp.security.SecurityUtils;
import ru.georgdeveloper.myapp.service.MailService;
import ru.georgdeveloper.myapp.service.UserService;
import ru.georgdeveloper.myapp.service.dto.AdminUserDTO;
import ru.georgdeveloper.myapp.service.dto.PasswordChangeDTO;
import ru.georgdeveloper.myapp.web.rest.errors.*;
import ru.georgdeveloper.myapp.web.rest.vm.KeyAndPasswordVM;
import ru.georgdeveloper.myapp.web.rest.vm.ManagedUserVM;

/**
 * REST контроллер для управления учетной записью текущего пользователя.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;
    private final UserService userService;
    private final MailService mailService;

    /**
     * Конструктор контроллера.
     */
    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * Регистрирует нового пользователя.
     * POST /register
     *
     * @param managedUserVM данные нового пользователя
     * @throws InvalidPasswordException если пароль не соответствует требованиям
     * @throws EmailAlreadyUsedException если email уже используется
     * @throws LoginAlreadyUsedException если логин уже используется
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        mailService.sendActivationEmail(user);
    }

    /**
     * Активирует зарегистрированного пользователя по ключу активации.
     * GET /activate
     *
     * @param key ключ активации
     * @throws RuntimeException если пользователь не может быть активирован
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("Пользователь с таким ключом активации не найден");
        }
    }

    /**
     * Получает данные текущего пользователя.
     * GET /account
     *
     * @return данные текущего пользователя
     * @throws RuntimeException если пользователь не может быть получен
     */
    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("Пользователь не найден"));
    }

    /**
     * Обновляет информацию текущего пользователя.
     * POST /account
     *
     * @param userDTO новые данные пользователя
     * @throws EmailAlreadyUsedException если email уже используется
     * @throws RuntimeException если логин пользователя не найден
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Текущий пользователь не найден"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.orElseThrow().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("Пользователь не найден");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    /**
     * Изменяет пароль текущего пользователя.
     * POST /account/change-password
     *
     * @param passwordChangeDto текущий и новый пароль
     * @throws InvalidPasswordException если новый пароль не соответствует требованиям
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * Инициирует сброс пароля пользователя (отправка email).
     * POST /account/reset-password/init
     *
     * @param mail email пользователя
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.orElseThrow());
        } else {
            // Логируем попытку сброса для несуществующего email
            LOG.warn("Запрошен сброс пароля для несуществующего email: {}", mail);
        }
    }

    /**
     * Завершает процесс сброса пароля пользователя.
     * POST /account/reset-password/finish
     *
     * @param keyAndPassword сгенерированный ключ и новый пароль
     * @throws InvalidPasswordException если пароль не соответствует требованиям
     * @throws RuntimeException если не удалось сбросить пароль
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("Пользователь с таким ключом сброса не найден");
        }
    }

    /**
     * Проверяет валидность длины пароля.
     */
    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }
}
