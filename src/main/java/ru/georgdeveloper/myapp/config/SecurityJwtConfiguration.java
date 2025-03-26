package ru.georgdeveloper.myapp.config;

import static ru.georgdeveloper.myapp.security.SecurityUtils.JWT_ALGORITHM;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import ru.georgdeveloper.myapp.management.SecurityMetersService;

@Configuration
public class SecurityJwtConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityJwtConfiguration.class);

    // Секретный ключ для JWT, закодированный в Base64, берется из свойств приложения
    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtKey;

    /**
     * Создает JwtDecoder с обработкой ошибок и метриками безопасности
     *
     * @param metersService сервис для сбора метрик безопасности
     * @return настроенный JwtDecoder с обработкой ошибок
     */
    @Bean
    public JwtDecoder jwtDecoder(SecurityMetersService metersService) {
        // Создаем базовый декодер JWT с использованием секретного ключа и алгоритма
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();

        // Возвращаем обертку над декодером с обработкой ошибок
        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                // Обработка различных типов ошибок JWT с записью метрик
                if (e.getMessage().contains("Invalid signature")) {
                    metersService.trackTokenInvalidSignature(); // Неверная подпись
                } else if (e.getMessage().contains("Jwt expired at")) {
                    metersService.trackTokenExpired(); // Просроченный токен
                } else if (
                    e.getMessage().contains("Invalid JWT serialization") ||
                    e.getMessage().contains("Malformed token") ||
                    e.getMessage().contains("Invalid unsecured/JWS/JWE")
                ) {
                    metersService.trackTokenMalformed(); // Некорректный формат токена
                } else {
                    LOG.error("Unknown JWT error {}", e.getMessage()); // Неизвестная ошибка
                }
                throw e; // Пробрасываем исключение дальше
            }
        };
    }

    /**
     * Создает JwtEncoder для генерации JWT токенов
     *
     * @return настроенный JwtEncoder
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        // Используем NimbusJwtEncoder с ImmutableSecret в качестве источника ключей
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    /**
     * Преобразует Base64-закодированный секретный ключ в объект SecretKey
     *
     * @return объект SecretKey для работы с JWT
     */
    private SecretKey getSecretKey() {
        // Декодируем ключ из Base64
        byte[] keyBytes = Base64.from(jwtKey).decode();
        // Создаем SecretKeySpec с указанием алгоритма
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }
}
