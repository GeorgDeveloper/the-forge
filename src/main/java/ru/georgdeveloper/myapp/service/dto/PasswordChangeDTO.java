package ru.georgdeveloper.myapp.service.dto;

import java.io.Serializable;

/**
 * DTO для представления данных, необходимых для изменения пароля - текущий и новый пароль.
 * Реализует Serializable для корректной сериализации/десериализации.
 */
public class PasswordChangeDTO implements Serializable {

    // Уникальный идентификатор версии для сериализации
    private static final long serialVersionUID = 1L;

    // Текущий пароль пользователя
    private String currentPassword;

    // Новый пароль, который пользователь хочет установить
    private String newPassword;

    /**
     * Пустой конструктор, необходимый для Jackson (библиотеки JSON-сериализации).
     */
    public PasswordChangeDTO() {
        // Явно объявленный пустой конструктор
    }

    /**
     * Основной конструктор для создания DTO изменения пароля.
     * @param currentPassword текущий пароль пользователя
     * @param newPassword новый пароль для установки
     */
    public PasswordChangeDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    /**
     * Получает текущий пароль пользователя.
     * @return текущий пароль
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * Устанавливает текущий пароль пользователя.
     * @param currentPassword текущий пароль для установки
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * Получает новый пароль пользователя.
     * @return новый пароль
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Устанавливает новый пароль пользователя.
     * @param newPassword новый пароль для установки
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
