package ru.georgdeveloper.myapp.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

/**
 * Конфигурация для настройки загрузки файлов.
 * Устанавливает максимальный размер файла до 50MB.
 */
@Configuration
public class MultipartConfig {

    /**
     * Настройка MultipartConfigElement для увеличения лимита размера файлов.
     *
     * @return MultipartConfigElement с настройками
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        // Максимальный размер одного файла: 50MB
        factory.setMaxFileSize(DataSize.ofMegabytes(50));

        // Максимальный размер всего запроса: 50MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(50));

        // Пороговое значение для записи на диск
        factory.setFileSizeThreshold(DataSize.ofMegabytes(1));

        return factory.createMultipartConfig();
    }

    /**
     * Настройка MultipartResolver для обработки multipart запросов.
     *
     * @return StandardServletMultipartResolver
     */
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
