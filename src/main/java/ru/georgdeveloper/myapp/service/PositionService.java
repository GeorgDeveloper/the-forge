package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.Position;

/**
 * Интерфейс сервиса для управления {@link ru.georgdeveloper.myapp.domain.Position}.
 */
public interface PositionService {
    /**
     * Сохранить должность.
     *
     * @param position сущность для сохранения.
     * @return сохраненная сущность.
     */
    Position save(Position position);

    /**
     * Обновить должность.
     *
     * @param position сущность для обновления.
     * @return обновленная сущность.
     */
    Position update(Position position);

    /**
     * Частично обновить должность.
     *
     * @param position сущность для частичного обновления.
     * @return обновленная сущность.
     */
    Optional<Position> partialUpdate(Position position);

    /**
     * Получить все должности.
     *
     * @param pageable информация о пагинации.
     * @return список сущностей.
     */
    Page<Position> findAll(Pageable pageable);

    /**
     * Получить должность по "id".
     *
     * @param id идентификатор сущности.
     * @return сущность.
     */
    Optional<Position> findOne(Long id);

    /**
     * Удалить должность по "id".
     *
     * @param id идентификатор сущности.
     */
    void delete(Long id);
}
