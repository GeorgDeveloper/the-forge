package ru.georgdeveloper.myapp.config;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@Profile({ JHipsterConstants.SPRING_PROFILE_PRODUCTION }) // Активируется только в production профиле
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {

    // Локации статических ресурсов в classpath
    protected static final String[] RESOURCE_LOCATIONS = { "classpath:/static/", "classpath:/static/content/", "classpath:/static/i18n/" };

    // Пути URL, по которым будут доступны статические ресурсы
    protected static final String[] RESOURCE_PATHS = { "/*.js", "/*.css", "/*.svg", "/*.png", "*.ico", "/content/**", "/i18n/*" };

    private final JHipsterProperties jhipsterProperties; // Свойства приложения из JHipster

    // Конструктор с внедрением зависимостей
    public StaticResourcesWebConfiguration(JHipsterProperties jHipsterProperties) {
        this.jhipsterProperties = jHipsterProperties;
    }

    /**
     * Метод для настройки обработчиков статических ресурсов
     *
     * @param registry реестр обработчиков ресурсов
     */
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // 1. Добавляем обработчики ресурсов
        ResourceHandlerRegistration resourceHandlerRegistration = appendResourceHandler(registry);
        // 2. Инициализируем обработчики с настройками
        initializeResourceHandler(resourceHandlerRegistration);
    }

    /**
     * Регистрирует обработчики для указанных путей ресурсов
     *
     * @param registry реестр обработчиков ресурсов
     * @return объект для дальнейшей настройки обработчиков
     */
    protected ResourceHandlerRegistration appendResourceHandler(ResourceHandlerRegistry registry) {
        return registry.addResourceHandler(RESOURCE_PATHS);
    }

    /**
     * Настраивает зарегистрированные обработчики ресурсов
     *
     * @param resourceHandlerRegistration объект для настройки обработчиков
     */
    protected void initializeResourceHandler(ResourceHandlerRegistration resourceHandlerRegistration) {
        resourceHandlerRegistration
            .addResourceLocations(RESOURCE_LOCATIONS) // Указываем расположение ресурсов
            .setCacheControl(getCacheControl()); // Устанавливаем политику кэширования
    }

    /**
     * Создает политику кэширования для статических ресурсов
     *
     * @return объект CacheControl с настройками кэширования
     */
    protected CacheControl getCacheControl() {
        return CacheControl.maxAge(getJHipsterHttpCacheProperty(), TimeUnit.DAYS).cachePublic(); // Время жизни кэша в днях // Разрешаем кэширование публичными прокси
    }

    /**
     * Получает время жизни кэша из настроек JHipster
     *
     * @return время жизни кэша в днях
     */
    private int getJHipsterHttpCacheProperty() {
        return jhipsterProperties.getHttp().getCache().getTimeToLiveInDays();
    }
}
