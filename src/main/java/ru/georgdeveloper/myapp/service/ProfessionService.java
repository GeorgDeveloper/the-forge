package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.Profession;

/**
 * Интерфейс сервиса для управления {@link ru.georgdeveloper.myapp.domain.Profession}.
 */
public interface ProfessionService {
    /**
     * Сохранить профессию.
     *
     * @param profession сущность для сохранения.
     * @return сохраненная сущность.
     */
    Profession save(Profession profession);

    /**
     * Обновить профессию.
     *
     * @param profession сущность для обновления.
     * @return обновленная сущность.
     */
    Profession update(Profession profession);

    /**
     * Частично обновить профессию.
     *
     * @param profession сущность для частичного обновления.
     * @return обновленная сущность.
     */
    Optional<Profession> partialUpdate(Profession profession);

    /**
     * Получить все профессии.
     *
     * @param pageable параметры пагинации.
     * @return список сущностей.
     */
    Page<Profession> findAll(Pageable pageable);

    /**
     * Получить профессию по идентификатору.
     *
     * @param id идентификатор сущности.
     * @return сущность.
     */
    Optional<Profession> findOne(Long id);

    /**
     * Удалить профессию по идентификатору.
     *
     * @param id идентификатор сущности.
     */
    void delete(Long id);
}
