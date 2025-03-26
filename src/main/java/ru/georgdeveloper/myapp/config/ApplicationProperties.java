package ru.georgdeveloper.myapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Свойства, специфичные для приложения The Forge.
 * <p>
 * Свойства настраиваются в файле {@code application.yml}.
 * Смотрите {@link tech.jhipster.config.JHipsterProperties} как хороший пример.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart = true;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }
    // jhipster-needle-application-properties-property-class
}
