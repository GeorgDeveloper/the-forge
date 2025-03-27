package ru.georgdeveloper.myapp.web.rest.errors;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause.ProblemDetailWithCauseBuilder;

/**
 * Исключение для обработки некорректных запросов (HTTP 400 Bad Request).
 * Содержит дополнительную информацию о сущности и ключе ошибки.
 *Наследуется от ErrorResponseException для интеграции со Spring Error Handling.
 */
@SuppressWarnings("java:S110") // Отключаем предупреждение о слишком глубоком дереве наследования
public class BadRequestAlertException extends ErrorResponseException {

    // Уникальный идентификатор версии класса для сериализации
    private static final long serialVersionUID = 1L;

    // Наименование сущности, связанной с ошибкой
    private final String entityName;

    // Ключ ошибки для интернационализации сообщений
    private final String errorKey;

    /**
     * Конструктор с параметрами по умолчанию.
     *
     * @param defaultMessage стандартное сообщение об ошибке
     * @param entityName наименование сущности, связанной с ошибкой
     * @param errorKey ключ ошибки для интернационализации
     */
    public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
    }

    /**
     * Основной конструктор с возможностью указания типа ошибки.
     *
     * @param type URI типа ошибки (для стандартизации)
     * @param defaultMessage стандартное сообщение об ошибке
     * @param entityName наименование сущности
     * @param errorKey ключ ошибки
     */
    public BadRequestAlertException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(
            HttpStatus.BAD_REQUEST, // Устанавливаем HTTP статус 400
            ProblemDetailWithCauseBuilder.instance()
                .withStatus(HttpStatus.BAD_REQUEST.value()) // Устанавливаем статус
                .withType(type) // Устанавливаем тип ошибки
                .withTitle(defaultMessage) // Устанавливаем заголовок
                .withProperty("message", "error." + errorKey) // Добавляем свойство с ключом ошибки
                .withProperty("params", entityName) // Добавляем свойство с именем сущности
                .build(), // Создаем объект ProblemDetail
            null // Причина ошибки (может быть null)
        );
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    /**
     * Возвращает имя сущности, связанной с ошибкой.
     *
     * @return имя сущности
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * Возвращает ключ ошибки для интернационализации.
     *
     * @return ключ ошибки
     */
    public String getErrorKey() {
        return errorKey;
    }

    /**
     * Возвращает детализированную информацию об ошибке с возможными причинами.
     *
     * @return объект ProblemDetailWithCause
     */
    public ProblemDetailWithCause getProblemDetailWithCause() {
        return (ProblemDetailWithCause) this.getBody();
    }
}
