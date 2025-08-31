package ru.georgdeveloper.myapp.security;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.georgdeveloper.myapp.config.Constants;

/**
 * Реализация интерфейса {@link AuditorAware} на основе Spring Security.
 * Используется для автоматического заполнения полей 'createdBy' и 'modifiedBy'
 * в сущностях, поддерживающих аудит.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    /**
     * Возвращает имя текущего пользователя для аудита.
     * Если пользователь не аутентифицирован, возвращает системного пользователя.
     *
     * @return Optional с именем текущего пользователя или системного пользователя
     */
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(
            SecurityUtils.getCurrentUserLogin() // Получаем текущего пользователя
                .orElse(Constants.SYSTEM) // Или системного пользователя, если нет аутентификации
        );
    }
}
