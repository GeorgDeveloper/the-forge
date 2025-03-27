package ru.georgdeveloper.myapp.web.rest.vm;

/**
 * View Model объект для хранения ключа пользователя и пароля.
 */
public class KeyAndPasswordVM {

    private String key; // Ключ (активации или сброса пароля)
    private String newPassword; // Новый пароль

    /**
     * Получает ключ.
     * @return текущий ключ
     */
    public String getKey() {
        return key;
    }

    /**
     * Устанавливает ключ.
     * @param key новый ключ
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Получает новый пароль.
     * @return новый пароль
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Устанавливает новый пароль.
     * @param newPassword новый пароль
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
