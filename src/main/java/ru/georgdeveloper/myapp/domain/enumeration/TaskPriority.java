package ru.georgdeveloper.myapp.domain.enumeration;

/**
 * Перечисление приоритетности Задач.
 */
public enum TaskPriority {
    LOW("НИЗКИЙ"),
    MEDIUM("СРЕДНИЙ"),
    HIGH("ВЫСОКИЙ"),
    CRITICAL("КРИТИЧНЫЙ");

    private String title;

    TaskPriority(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
