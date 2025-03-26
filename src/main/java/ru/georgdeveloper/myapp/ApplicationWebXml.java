package ru.georgdeveloper.myapp;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import tech.jhipster.config.DefaultProfileUtil;

/**
 * Это вспомогательный Java-класс, который предоставляет альтернативу созданию {@code web.xml}.
 * Он будет вызываться только при развертывании приложения в контейнере сервлетов, таком как Tomcat, JBoss и т.д.
 */
public class ApplicationWebXml extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        // set a default to use when no profile is configured.
        DefaultProfileUtil.addDefaultProfile(application.application());
        return application.sources(TheForgeApp.class);
    }
}
