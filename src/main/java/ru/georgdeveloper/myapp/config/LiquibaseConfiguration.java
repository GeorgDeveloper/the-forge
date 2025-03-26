package ru.georgdeveloper.myapp.config;

import java.util.concurrent.Executor;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.liquibase.SpringLiquibaseUtil;

@Configuration
public class LiquibaseConfiguration {

    // Логгер для записи событий конфигурации
    private static final Logger LOG = LoggerFactory.getLogger(LiquibaseConfiguration.class);

    private final Environment env; // Окружение Spring для доступа к профилям и свойствам

    // Конструктор с внедрением зависимости Environment
    public LiquibaseConfiguration(Environment env) {
        this.env = env;
    }

    /**
     * Создает и настраивает бин SpringLiquibase для управления миграциями базы данных.
     *
     * @param executor Executor для асинхронного запуска Liquibase (если требуется)
     * @param liquibaseProperties Свойства конфигурации Liquibase
     * @param liquibaseDataSource Провайдер источника данных для Liquibase
     * @param dataSource Провайдер основного источника данных
     * @param applicationProperties Свойства приложения
     * @param dataSourceProperties Свойства источника данных
     * @return настроенный бин SpringLiquibase
     */
    @Bean
    public SpringLiquibase liquibase(
        @Qualifier("taskExecutor") Executor executor,
        LiquibaseProperties liquibaseProperties,
        @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource,
        ObjectProvider<DataSource> dataSource,
        ApplicationProperties applicationProperties,
        DataSourceProperties dataSourceProperties
    ) {
        SpringLiquibase liquibase;

        // Выбор между синхронным и асинхронным запуском Liquibase
        if (Boolean.TRUE.equals(applicationProperties.getLiquibase().getAsyncStart())) {
            // Асинхронная инициализация Liquibase (для ускорения старта приложения)
            liquibase = SpringLiquibaseUtil.createAsyncSpringLiquibase(
                this.env,
                executor,
                liquibaseDataSource.getIfAvailable(),
                liquibaseProperties,
                dataSource.getIfUnique(),
                dataSourceProperties
            );
        } else {
            // Синхронная инициализация Liquibase
            liquibase = SpringLiquibaseUtil.createSpringLiquibase(
                liquibaseDataSource.getIfAvailable(),
                liquibaseProperties,
                dataSource.getIfUnique(),
                dataSourceProperties
            );
        }

        // Основные настройки Liquibase
        liquibase.setChangeLog("classpath:config/liquibase/master.xml"); // Путь к главному файлу изменений

        // Настройка контекстов выполнения миграций
        if (!CollectionUtils.isEmpty(liquibaseProperties.getContexts())) {
            liquibase.setContexts(StringUtils.collectionToCommaDelimitedString(liquibaseProperties.getContexts()));
        }

        // Настройка схемы и табличного пространства
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());

        // Настройка имен таблиц для журналирования изменений
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());

        // Настройка поведения при запуске
        liquibase.setDropFirst(liquibaseProperties.isDropFirst()); // Удалять ли схему перед применением изменений

        // Настройка фильтрации по меткам
        if (!CollectionUtils.isEmpty(liquibaseProperties.getLabelFilter())) {
            liquibase.setLabelFilter(StringUtils.collectionToCommaDelimitedString(liquibaseProperties.getLabelFilter()));
        }

        // Дополнительные параметры
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());

        // Проверка профиля для отключения Liquibase
        if (env.matchesProfiles(JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE)) {
            liquibase.setShouldRun(false); // Отключить выполнение Liquibase
        } else {
            liquibase.setShouldRun(liquibaseProperties.isEnabled()); // Включить/отключить в зависимости от настроек
            LOG.debug("Configuring Liquibase"); // Логирование факта конфигурации
        }

        return liquibase;
    }
}
