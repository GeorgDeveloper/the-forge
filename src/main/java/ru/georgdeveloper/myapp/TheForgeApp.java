package ru.georgdeveloper.myapp;

import jakarta.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import ru.georgdeveloper.myapp.config.ApplicationProperties;
import ru.georgdeveloper.myapp.config.CRLFLogConverter;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;

/**
 * Главный класс приложения TheForge.
 * <p>
 * Этот класс является точкой входа в Spring Boot приложение.
 * Он настраивает и запускает приложение, а также обрабатывает профили Spring.
 */
@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
public class TheForgeApp {

    // Логгер для записи информации о запуске приложения
    private static final Logger LOG = LoggerFactory.getLogger(TheForgeApp.class);

    // Окружение Spring, содержащее конфигурационные свойства
    private final Environment env;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param env окружение Spring
     */
    public TheForgeApp(Environment env) {
        this.env = env;
    }

    /**
     * Инициализирует приложение TheForge.
     * <p>
     * Профили Spring могут быть настроены с помощью аргумента командной строки:
     * --spring.profiles.active=ваш-активный-профиль
     * <p>
     * Дополнительную информацию о работе профилей в JHipster можно найти на:
     * <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     * <p>
     * Метод проверяет недопустимые комбинации профилей.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            LOG.error("Некорректная конфигурация приложения! Нельзя одновременно использовать " + "профили 'dev' и 'prod'.");
        }
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)
        ) {
            LOG.error("Некорректная конфигурация приложения! Нельзя одновременно использовать " + "профили 'dev' и 'cloud'.");
        }
    }

    /**
     * Главный метод, используемый для запуска приложения.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TheForgeApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    /**
     * Логирует информацию о запуске приложения.
     * <p>
     * Выводит в лог URL-адреса для доступа к приложению и активные профили.
     *
     * @param env окружение Spring
     */
    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String applicationName = env.getProperty("spring.application.name");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOG.warn("Не удалось определить имя хоста, используется 'localhost'");
        }
        LOG.info(
            CRLFLogConverter.CRLF_SAFE_MARKER,
            """

            ----------------------------------------------------------
            \tПриложение '{}' запущено! URL-адреса для доступа:
            \tЛокальный: \t{}://localhost:{}{}
            \tВнешний: \t{}://{}:{}{}
            \tПрофили: \t{}
            ----------------------------------------------------------""",
            applicationName,
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
        );
    }
}
