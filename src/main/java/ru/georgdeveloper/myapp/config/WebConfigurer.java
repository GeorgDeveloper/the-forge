package ru.georgdeveloper.myapp.config;

import static java.net.URLDecoder.decode;

import jakarta.servlet.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.*;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import tech.jhipster.config.JHipsterProperties;

/**
 * Конфигурация веб-приложения с использованием Servlet 3.0 API.
 * Настраивает:
 * - Инициализацию ServletContext
 * - Кастомизацию веб-сервера
 * - CORS фильтр
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer, WebServerFactoryCustomizer<WebServerFactory> {

    private static final Logger LOG = LoggerFactory.getLogger(WebConfigurer.class);

    private final Environment env; // Доступ к окружению Spring
    private final JHipsterProperties jHipsterProperties; // Настройки JHipster

    public WebConfigurer(Environment env, JHipsterProperties jHipsterProperties) {
        this.env = env;
        this.jHipsterProperties = jHipsterProperties;
    }

    /**
     * Инициализация ServletContext при запуске приложения
     */
    @Override
    public void onStartup(ServletContext servletContext) {
        if (env.getActiveProfiles().length != 0) {
            LOG.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }
        LOG.info("Web application fully configured");
    }

    /**
     * Кастомизация веб-сервера: MIME-типы, корневая директория, кэш
     */
    @Override
    public void customize(WebServerFactory server) {
        // Установка расположения статических ресурсов при запуске в IDE или через bootRun
        setLocationForStaticAssets(server);
    }

    /**
     * Устанавливает расположение статических ресурсов для веб-сервера
     */
    private void setLocationForStaticAssets(WebServerFactory server) {
        if (server instanceof ConfigurableServletWebServerFactory servletWebServer) {
            File root;
            String prefixPath = resolvePathPrefix();
            root = Path.of(prefixPath + "build/resources/main/static/").toFile();
            if (root.exists() && root.isDirectory()) {
                servletWebServer.setDocumentRoot(root); // Устанавливаем корневую директорию
            }
        }
    }

    /**
     * Определяет префикс пути к статическим ресурсам
     */
    private String resolvePathPrefix() {
        String fullExecutablePath = decode(this.getClass().getResource("").getPath(), StandardCharsets.UTF_8);
        String rootPath = Path.of(".").toUri().normalize().getPath();
        String extractedPath = fullExecutablePath.replace(rootPath, "");
        int extractionEndIndex = extractedPath.indexOf("build/");
        if (extractionEndIndex <= 0) {
            return "";
        }
        return extractedPath.substring(0, extractionEndIndex);
    }

    /**
     * Создает и настраивает CORS фильтр
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = jHipsterProperties.getCors();

        if (config != null) {
            var allowedOrigins = config.getAllowedOrigins();
            if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
                LOG.debug("Registering CORS filter");
                source.registerCorsConfiguration("/api/**", config);
                source.registerCorsConfiguration("/management/**", config);
                source.registerCorsConfiguration("/v3/api-docs", config);
                source.registerCorsConfiguration("/swagger-ui/**", config);
            }
        }

        return new CorsFilter(source);
    }
}
