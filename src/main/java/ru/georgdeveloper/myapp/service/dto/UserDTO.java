package ru.georgdeveloper.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import ru.georgdeveloper.myapp.domain.User;

/**
 * DTO представляющий пользователя только с публичными атрибутами.
 * Используется для безопасной передачи данных о пользователе в публичных API.
 */
public class UserDTO implements Serializable {

    // Идентификатор для сериализации
    private static final long serialVersionUID = 1L;

    // ID пользователя
    private Long id;

    // Логин пользователя
    private String login;

    /**
     * Пустой конструктор, необходимый для Jackson.
     */
    public UserDTO() {
        // Требуется для десериализации JSON
    }

    /**
     * Конструктор для преобразования User в UserDTO.
     * @param user сущность пользователя из базы данных
     */
    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        // Можно добавить другие публичные поля при необходимости
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Переопределение equals для корректного сравнения объектов UserDTO.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;
        // Если ID нет у одного из объектов - считаем их разными
        if (userDTO.getId() == null || getId() == null) {
            return false;
        }
        // Сравниваем по ID и логину
        return Objects.equals(getId(), userDTO.getId()) && Objects.equals(getLogin(), userDTO.getLogin());
    }

    /**
     * Переопределение hashCode для консистентности с equals.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLogin());
    }

    /**
     * Переопределение toString для удобного логирования.
     */
    @Override
    public String toString() {
        return "UserDTO{" + "id='" + id + '\'' + ", login='" + login + '\'' + "}";
    }
}
