package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.ProtectiveEquipment;

/**
 * Интерфейс сервиса для управления {@link ru.georgdeveloper.myapp.domain.ProtectiveEquipment}.
 */
public interface ProtectiveEquipmentService {
    /**
     * Сохранить средство защиты.
     *
     * @param protectiveEquipment сущность для сохранения.
     * @return сохраненная сущность.
     */
    ProtectiveEquipment save(ProtectiveEquipment protectiveEquipment);

    /**
     * Обновить средство защиты.
     *
     * @param protectiveEquipment сущность для обновления.
     * @return обновленная сущность.
     */
    ProtectiveEquipment update(ProtectiveEquipment protectiveEquipment);

    /**
     * Частично обновить средство защиты.
     *
     * @param protectiveEquipment сущность для частичного обновления.
     * @return обновленная сущность.
     */
    Optional<ProtectiveEquipment> partialUpdate(ProtectiveEquipment protectiveEquipment);

    /**
     * Получить все средства защиты.
     *
     * @param pageable параметры пагинации.
     * @return список сущностей.
     */
    Page<ProtectiveEquipment> findAll(Pageable pageable);

    /**
     * Получить средство защиты по идентификатору.
     *
     * @param id идентификатор сущности.
     * @return сущность.
     */
    Optional<ProtectiveEquipment> findOne(Long id);

    /**
     * Удалить средство защиты по идентификатору.
     *
     * @param id идентификатор сущности.
     */
    void delete(Long id);
}
