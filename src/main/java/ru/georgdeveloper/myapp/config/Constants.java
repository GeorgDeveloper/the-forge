package ru.georgdeveloper.myapp.config;

/**
 * Константы приложения.
 */
public final class Constants {

    // Regex для допустимых логинов
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "ru";

    private Constants() {}
}
