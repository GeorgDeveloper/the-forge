package ru.georgdeveloper.myapp.security;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Утилитарный класс для работы с Spring Security.
 * Предоставляет методы для получения информации о текущем пользователе и проверки прав доступа.
 */
public final class SecurityUtils {

    // Алгоритм подписи JWT токенов
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    // Ключ для хранения authorities в JWT токене
    public static final String AUTHORITIES_KEY = "auth";

    // Приватный конструктор для запрета создания экземпляров утилитного класса
    private SecurityUtils() {}

    /**
     * Получает логин текущего аутентифицированного пользователя.
     * @return Optional с логином пользователя, если аутентифицирован
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    /**
     * Извлекает principal из объекта аутентификации.
     * Поддерживает разные типы principal:
     * - UserDetails (стандартный объект Spring Security)
     * - Jwt (OAuth 2.0 JWT токен)
     * - String (простое имя пользователя)
     */
    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    /**
     * Получает JWT токен текущего аутентифицированного пользователя.
     * @return Optional с JWT токеном, если пользователь аутентифицирован через JWT
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .filter(authentication -> authentication.getCredentials() instanceof String)
            .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Проверяет, аутентифицирован ли текущий пользователь.
     * @return true если пользователь аутентифицирован и не анонимный
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    }

    /**
     * Проверяет, есть ли у текущего пользователя хотя бы одно из указанных прав.
     * @param authorities список прав для проверки
     * @return true если есть хотя бы одно из прав
     */
    public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (
            authentication != null && getAuthorities(authentication).anyMatch(authority -> Arrays.asList(authorities).contains(authority))
        );
    }

    /**
     * Проверяет, что у текущего пользователя нет ни одного из указанных прав.
     * @param authorities список прав для проверки
     * @return true если нет ни одного из прав
     */
    public static boolean hasCurrentUserNoneOfAuthorities(String... authorities) {
        return !hasCurrentUserAnyOfAuthorities(authorities);
    }

    /**
     * Проверяет, есть ли у текущего пользователя конкретное право.
     * @param authority право для проверки
     * @return true если право есть
     */
    public static boolean hasCurrentUserThisAuthority(String authority) {
        return hasCurrentUserAnyOfAuthorities(authority);
    }

    /**
     * Преобразует authorities из Authentication в поток строк.
     */
    private static Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    }
}
