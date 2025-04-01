package ru.georgdeveloper.myapp.domain.enumeration;

public enum AccessLevel {
    OWNER("Владелец"), // Владелец команды (может редактировать и делиться)
    VIEWER("Просмотр"); // Пользователь с доступом на просмотр

    private String title;

    AccessLevel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
