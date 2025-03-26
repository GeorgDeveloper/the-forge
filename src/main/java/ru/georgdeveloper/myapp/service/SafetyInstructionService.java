package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.SafetyInstruction;

/**
 * Интерфейс сервиса для управления {@link ru.georgdeveloper.myapp.domain.SafetyInstruction}.
 */
public interface SafetyInstructionService {
    /**
     * Сохранить инструкцию по технике безопасности.
     *
     * @param safetyInstruction сохраняемая сущность.
     * @return сохраненная сущность.
     */
    SafetyInstruction save(SafetyInstruction safetyInstruction);

    /**
     * Обновить инструкцию по технике безопасности.
     *
     * @param safetyInstruction обновляемая сущность.
     * @return обновленная сущность.
     */
    SafetyInstruction update(SafetyInstruction safetyInstruction);

    /**
     * Частично обновить инструкцию по технике безопасности.
     *
     * @param safetyInstruction сущность для частичного обновления.
     * @return обновленная сущность.
     */
    Optional<SafetyInstruction> partialUpdate(SafetyInstruction safetyInstruction);

    /**
     * Получить все инструкции по технике безопасности.
     *
     * @param pageable параметры постраничного вывода.
     * @return список сущностей.
     */
    Page<SafetyInstruction> findAll(Pageable pageable);

    /**
     * Получить инструкцию по технике безопасности по идентификатору.
     *
     * @param id идентификатор сущности.
     * @return найденная сущность.
     */
    Optional<SafetyInstruction> findOne(Long id);

    /**
     * Удалить инструкцию по технике безопасности.
     *
     * @param id идентификатор удаляемой сущности.
     */
    void delete(Long id);
}
