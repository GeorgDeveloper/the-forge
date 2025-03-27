package ru.georgdeveloper.myapp.web.rest.vm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * View Model объект для хранения учетных данных пользователя.
 */
public class LoginVM {

    @NotNull
    @Size(min = 1, max = 50)
    private String username; // Логин пользователя

    @NotNull
    @Size(min = 4, max = 100)
    private String password; // Пароль пользователя

    private boolean rememberMe; // Флаг "Запомнить меня"

    /**
     * Получает логин пользователя.
     * @return текущий логин
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает логин пользователя.
     * @param username новый логин
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Получает пароль пользователя.
     * @return текущий пароль
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль пользователя.
     * @param password новый пароль
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Проверяет, активирован ли флаг "Запомнить меня".
     * @return true если флаг активирован
     */
    public boolean isRememberMe() {
        return rememberMe;
    }

    /**
     * Устанавливает флаг "Запомнить меня".
     * @param rememberMe новое значение флага
     */
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoginVM{" +
            "username='" + username + '\'' +
            ", rememberMe=" + rememberMe +
            '}';
    }
}
