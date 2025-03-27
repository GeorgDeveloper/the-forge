package ru.georgdeveloper.myapp.web.rest.vm;

import jakarta.validation.constraints.Size;
import ru.georgdeveloper.myapp.service.dto.AdminUserDTO;

/**
 * View Model объект, расширяющий AdminUserDTO, предназначенный для использования
 * в пользовательском интерфейсе управления пользователями.
 */
public class ManagedUserVM extends AdminUserDTO {

    // Минимальная длина пароля
    public static final int PASSWORD_MIN_LENGTH = 4;

    // Максимальная длина пароля
    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password; // Пароль пользователя

    /**
     * Конструктор по умолчанию.
     * Требуется для корректной работы Jackson.
     */
    public ManagedUserVM() {
        // Пустой конструктор необходим для Jackson
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
     * @throws IllegalArgumentException если пароль не соответствует ограничениям длины
     */
    public void setPassword(String password) {
        this.password = password;
    }

    // prettier-ignore
    /**
     * Возвращает строковое представление объекта.
     * @return строковое представление, включая данные родительского класса
     */
    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
