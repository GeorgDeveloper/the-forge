package ru.georgdeveloper.myapp.management;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

/**
 * Сервис для мониторинга и сбора метрик безопасности, связанных с обработкой токенов аутентификации.
 * Использует Micrometer для сбора и экспорта метрик.
 */
@Service
public class SecurityMetersService {

    // Название метрики для невалидных токенов
    public static final String INVALID_TOKENS_METER_NAME = "security.authentication.invalid-tokens";

    // Описание метрики
    public static final String INVALID_TOKENS_METER_DESCRIPTION =
        "Indicates validation error count of the tokens presented by the clients.";

    // Базовая единица измерения метрики
    public static final String INVALID_TOKENS_METER_BASE_UNIT = "errors";

    // Название тега для указания причины ошибки
    public static final String INVALID_TOKENS_METER_CAUSE_DIMENSION = "cause";

    // Счетчики для различных типов ошибок токенов:
    private final Counter tokenInvalidSignatureCounter; // Неверная подпись токена
    private final Counter tokenExpiredCounter; // Просроченный токен
    private final Counter tokenUnsupportedCounter; // Неподдерживаемый токен
    private final Counter tokenMalformedCounter; // Некорректно сформированный токен

    /**
     * Конструктор сервиса, инициализирует счетчики метрик.
     * @param registry реестр метрик Micrometer, куда регистрируются счетчики
     */
    public SecurityMetersService(MeterRegistry registry) {
        // Инициализация счетчиков с указанием причин ошибок
        this.tokenInvalidSignatureCounter = invalidTokensCounterForCauseBuilder("invalid-signature").register(registry);
        this.tokenExpiredCounter = invalidTokensCounterForCauseBuilder("expired").register(registry);
        this.tokenUnsupportedCounter = invalidTokensCounterForCauseBuilder("unsupported").register(registry);
        this.tokenMalformedCounter = invalidTokensCounterForCauseBuilder("malformed").register(registry);
    }

    /**
     * Создает билдер для счетчика невалидных токенов с указанной причиной.
     * @param cause причина невалидности токена
     * @return билдер счетчика Counter.Builder
     */
    private Counter.Builder invalidTokensCounterForCauseBuilder(String cause) {
        return Counter.builder(INVALID_TOKENS_METER_NAME)
            .baseUnit(INVALID_TOKENS_METER_BASE_UNIT) // Устанавливает единицу измерения
            .description(INVALID_TOKENS_METER_DESCRIPTION) // Устанавливает описание
            .tag(INVALID_TOKENS_METER_CAUSE_DIMENSION, cause); // Добавляет тег с причиной
    }

    /**
     * Увеличивает счетчик токенов с неверной подписью.
     */
    public void trackTokenInvalidSignature() {
        this.tokenInvalidSignatureCounter.increment();
    }

    /**
     * Увеличивает счетчик просроченных токенов.
     */
    public void trackTokenExpired() {
        this.tokenExpiredCounter.increment();
    }

    /**
     * Увеличивает счетчик неподдерживаемых токенов.
     */
    public void trackTokenUnsupported() {
        this.tokenUnsupportedCounter.increment();
    }

    /**
     * Увеличивает счетчик некорректно сформированных токенов.
     */
    public void trackTokenMalformed() {
        this.tokenMalformedCounter.increment();
    }
}
