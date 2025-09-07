package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.ProcedureDocument;

/**
 * Сервисный интерфейс для управления сущностью {@link ProcedureDocument}.
 */
public interface ProcedureDocumentService {
    /**
     * Сохраняет процедуру и документацию.
     *
     * @param procedureDocument сущность для сохранения.
     * @return сохраненная сущность.
     */
    ProcedureDocument save(ProcedureDocument procedureDocument);

    /**
     * Обновляет процедуру и документацию.
     *
     * @param procedureDocument сущность для обновления.
     * @return обновленная сущность.
     */
    ProcedureDocument update(ProcedureDocument procedureDocument);

    /**
     * Частично обновляет процедуру и документацию.
     *
     * @param procedureDocument сущность для частичного обновления.
     * @return частично обновленная сущность.
     */
    Optional<ProcedureDocument> partialUpdate(ProcedureDocument procedureDocument);

    /**
     * Получает все процедуры и документации.
     *
     * @param pageable информация о пагинации.
     * @return список процедур и документаций.
     */
    Page<ProcedureDocument> findAll(Pageable pageable);

    /**
     * Получает процедуру и документацию по ID.
     *
     * @param id ID сущности.
     * @return сущность или пустой Optional.
     */
    Optional<ProcedureDocument> findOne(Long id);

    /**
     * Удаляет процедуру и документацию по ID.
     *
     * @param id ID сущности для удаления.
     */
    void delete(Long id);
}
