package ru.georgdeveloper.myapp.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import ru.georgdeveloper.myapp.security.*;
import ru.georgdeveloper.myapp.web.filter.SpaWebFilter;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableMethodSecurity(securedEnabled = true) // Включает аннотацию @Secured для методов
public class SecurityConfiguration {

    private final JHipsterProperties jHipsterProperties; // Свойства приложения из JHipster

    // Конструктор с внедрением зависимостей
    public SecurityConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    /**
     * Бин для кодирования паролей с использованием BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Основная конфигурация цепочки фильтров безопасности
     *
     * @param http объект HttpSecurity для настройки
     * @param mvc билдер для создания MvcRequestMatcher
     * @return настроенная цепочка фильтров безопасности
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            // Настройка CORS (Cross-Origin Resource Sharing) с параметрами по умолчанию
            .cors(withDefaults())
            // Отключаем CSRF защиту, так как используется JWT
            .csrf(csrf -> csrf.disable())
            // Добавляем кастомный фильтр для SPA после BasicAuthenticationFilter
            .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
            // Конфигурация заголовков безопасности
            .headers(headers ->
                headers
                    // Политика безопасности контента (CSP)
                    .contentSecurityPolicy(csp -> csp.policyDirectives(jHipsterProperties.getSecurity().getContentSecurityPolicy()))
                    // Защита от clickjacking - разрешаем фреймы только с того же origin
                    .frameOptions(FrameOptionsConfig::sameOrigin)
                    // Политика Referrer для защиты от утечки информации
                    .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                    // Политика разрешений для браузерных API
                    .permissionsPolicyHeader(permissions ->
                        permissions.policy(
                            "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"
                        )
                    )
            )
            // Настройка авторизации запросов
            .authorizeHttpRequests(authz ->
                // prettier-ignore
                authz
                    // Разрешаем статические ресурсы
                    .requestMatchers(mvc.pattern("/index.html"), mvc.pattern("/*.js"), mvc.pattern("/*.txt"), mvc.pattern("/*.json"), mvc.pattern("/*.map"), mvc.pattern("/*.css")).permitAll()
                    .requestMatchers(mvc.pattern("/*.ico"), mvc.pattern("/*.png"), mvc.pattern("/*.svg"), mvc.pattern("/*.webapp")).permitAll()
                    .requestMatchers(mvc.pattern("/app/**")).permitAll()
                    .requestMatchers(mvc.pattern("/i18n/**")).permitAll()
                    .requestMatchers(mvc.pattern("/content/**")).permitAll()

                    // Разрешаем Swagger UI
                    .requestMatchers(mvc.pattern("/swagger-ui/**")).permitAll()

                    // Разрешаем аутентификацию
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/authenticate")).permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/authenticate")).permitAll()

                    // Разрешаем регистрацию и восстановление пароля
                    .requestMatchers(mvc.pattern("/api/register")).permitAll()
                    .requestMatchers(mvc.pattern("/api/activate")).permitAll()
                    .requestMatchers(mvc.pattern("/api/account/reset-password/init")).permitAll()
                    .requestMatchers(mvc.pattern("/api/account/reset-password/finish")).permitAll()

                    // Админские endpoints требуют роли ADMIN
                    .requestMatchers(mvc.pattern("/api/admin/**")).hasAuthority(AuthoritiesConstants.ADMIN)

                    // Все остальные API endpoints требуют аутентификации
                    .requestMatchers(mvc.pattern("/api/**")).authenticated()

                    // Документация API доступна только админам
                    .requestMatchers(mvc.pattern("/v3/api-docs/**")).hasAuthority(AuthoritiesConstants.ADMIN)

                    // Health checks доступны всем
                    .requestMatchers(mvc.pattern("/management/health")).permitAll()
                    .requestMatchers(mvc.pattern("/management/health/**")).permitAll()
                    .requestMatchers(mvc.pattern("/management/info")).permitAll()
                    .requestMatchers(mvc.pattern("/management/prometheus")).permitAll()

                    // Остальные management endpoints требуют роли ADMIN
                    .requestMatchers(mvc.pattern("/management/**")).hasAuthority(AuthoritiesConstants.ADMIN)
            )
            // Используем stateless сессии (так как используем JWT)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Обработка исключений безопасности
            .exceptionHandling(exceptions ->
                exceptions
                    // Точка входа для аутентификации через Bearer Token
                    .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                    // Обработчик отказа в доступе
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
            )
            // Настройка OAuth2 Resource Server с JWT
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));

        return http.build();
    }

    /**
     * Билдер для создания MvcRequestMatcher
     *
     * @param introspector интроспектор для маппинга запросов
     * @return билдер MvcRequestMatcher
     */
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
}
