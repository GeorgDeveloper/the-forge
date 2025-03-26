package ru.georgdeveloper.myapp.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Исключение, выбрасываемое при попытке аутентификации неактивированного пользователя.
 * Наследуется от AuthenticationException Spring Security.
 */
public class UserNotActivatedException extends AuthenticationException {

    // Версия класса для сериализации
    private static final long serialVersionUID = 1L;

    /**
     * Конструктор с сообщением об ошибке.
     * @param message описание причины исключения
     */
    public UserNotActivatedException(String message) {
        super(message);
    }

    /**
     * Конструктор с сообщением об ошибке и причиной.
     * @param message описание причины исключения
     * @param t исключение-причина
     */
    public UserNotActivatedException(String message, Throwable t) {
        super(message, t);
    }
}
