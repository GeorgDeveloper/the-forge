package ru.georgdeveloper.myapp.domain.enumeration;

/**
 * Перечисления статусов для Задач
 */
public enum TaskStatus {
    TODO("СОЗДАНА"),
    IN_PROGRESS("В ПРОЦЕССЕ"),
    DONE("ЗАВЕРШЕНО");

    private String title;

    TaskStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
