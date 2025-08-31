package ru.georgdeveloper.myapp.config;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgreSqlTestContainer implements SqlTestContainer, AutoCloseable {

    private static class ContainerHolder {

        static final PostgreSQLContainer<?> INSTANCE = createContainer();

        private static PostgreSQLContainer<?> createContainer() {
            @SuppressWarnings("resource")
            PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"))
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
            container.start();
            return container;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Контейнер инициализируется лениво при первом обращении
        // через механизм класс-холдера
    }

    @Override
    public void destroy() throws Exception {
        close();
    }

    @Override
    public void close() {
        if (ContainerHolder.INSTANCE != null && ContainerHolder.INSTANCE.isRunning()) {
            ContainerHolder.INSTANCE.close();
        }
    }

    @Override
    public PostgreSQLContainer<?> getTestContainer() {
        return ContainerHolder.INSTANCE;
    }
}
