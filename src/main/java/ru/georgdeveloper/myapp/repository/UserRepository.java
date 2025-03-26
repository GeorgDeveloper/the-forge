package ru.georgdeveloper.myapp.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.User;

/**
 * Spring Data JPA репозиторий для сущности {@link User}.
 * Предоставляет методы для работы с пользователями системы, включая методы для:
 * - активации учетных записей
 * - сброса пароля
 * - аутентификации
 * - управления правами доступа
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Находит пользователя по ключу активации.
     * @param activationKey ключ активации учетной записи
     * @return Optional с пользователем, если найден
     */
    Optional<User> findOneByActivationKey(String activationKey);

    /**
     * Находит всех неактивированных пользователей с ненулевым ключом активации,
     * созданных до указанной даты.
     * Используется для очистки просроченных регистраций.
     * @param dateTime крайний срок создания учетной записи
     * @return список неактивированных пользователей
     */
    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    /**
     * Находит пользователя по ключу сброса пароля.
     * @param resetKey ключ сброса пароля
     * @return Optional с пользователем, если найден
     */
    Optional<User> findOneByResetKey(String resetKey);

    /**
     * Находит пользователя по email (без учета регистра).
     * @param email адрес электронной почты
     * @return Optional с пользователем, если найден
     */
    Optional<User> findOneByEmailIgnoreCase(String email);

    /**
     * Находит пользователя по логину.
     * @param login имя пользователя (логин)
     * @return Optional с пользователем, если найден
     */
    Optional<User> findOneByLogin(String login);

    /**
     * Находит пользователя по логину с загруженными правами доступа (authorities).
     * Использует EntityGraph для эффективной загрузки связанных сущностей.
     * @param login имя пользователя (логин)
     * @return Optional с пользователем и его правами, если найден
     */
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    /**
     * Находит пользователя по email (без учета регистра) с загруженными правами доступа.
     * Использует EntityGraph для эффективной загрузки связанных сущностей.
     * @param email адрес электронной почты
     * @return Optional с пользователем и его правами, если найден
     */
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    /**
     * Находит всех активированных пользователей с пагинацией.
     * Исключает пользователей с null ID (на всякий случай).
     * @param pageable параметры пагинации
     * @return страница активированных пользователей
     */
    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
}
