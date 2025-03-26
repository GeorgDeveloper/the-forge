package ru.georgdeveloper.myapp.security;

import java.util.*;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Authority;
import ru.georgdeveloper.myapp.domain.User;
import ru.georgdeveloper.myapp.repository.UserRepository;

/**
 * Сервис аутентификации пользователей из базы данных.
 * Реализует интерфейс UserDetailsService Spring Security.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    /**
     * Конструктор с внедрением зависимости UserRepository.
     * @param userRepository репозиторий для работы с пользователями
     */
    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Загружает пользователя по имени пользователя или email.
     * @param login имя пользователя или email
     * @return UserDetails с данными пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String login) {
        LOG.debug("Authenticating {}", login);

        // Проверяем, является ли входная строка email-адресом
        if (new EmailValidator().isValid(login, null)) {
            return userRepository
                .findOneWithAuthoritiesByEmailIgnoreCase(login)
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
        }

        // Если не email, обрабатываем как логин (в нижнем регистре)
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return userRepository
            .findOneWithAuthoritiesByLogin(lowercaseLogin)
            .map(user -> createSpringSecurityUser(lowercaseLogin, user))
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
    }

    /**
     * Создает объект пользователя для Spring Security.
     * @param lowercaseLogin логин в нижнем регистре
     * @param user сущность пользователя из базы данных
     * @return UserDetails для Spring Security
     * @throws UserNotActivatedException если пользователь не активирован
     */
    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        // Проверяем активацию пользователя
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }

        // Преобразуем authorities пользователя в формат Spring Security
        List<SimpleGrantedAuthority> grantedAuthorities = user
            .getAuthorities()
            .stream()
            .map(Authority::getName) // Получаем название роли/права
            .map(SimpleGrantedAuthority::new) // Создаем объект SimpleGrantedAuthority
            .toList();

        // Создаем и возвращаем UserDetails объект
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuthorities);
    }
}
