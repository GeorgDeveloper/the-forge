package ru.georgdeveloper.myapp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import ru.georgdeveloper.myapp.config.AsyncSyncConfiguration;
import ru.georgdeveloper.myapp.config.EmbeddedSQL;
import ru.georgdeveloper.myapp.config.JacksonConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { TheForgeApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
