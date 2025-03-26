package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.Training;

/**
 * Интерфейс сервиса для управления {@link ru.georgdeveloper.myapp.domain.Training}.
 */
public interface TrainingService {
    /**
     * Сохранить инструктаж.
     *
     * @param training сохраняемый объект инструктаж.
     * @return сохраненный объект инструктаж.
     */
    Training save(Training training);

    /**
     * Обновить инструктаж.
     *
     * @param training обновляемый объект инструктаж.
     * @return обновленный объект инструктаж.
     */
    Training update(Training training);

    /**
     * Частично обновить инструктаж.
     *
     * @param training объект для частичного обновления.
     * @return частично обновленный объект инструктаж.
     */
    Optional<Training> partialUpdate(Training training);

    /**
     * Получить все инструктажи.
     *
     * @param pageable параметры разбиения на страницы.
     * @return страница с перечнем инструктажей.
     */
    Page<Training> findAll(Pageable pageable);

    /**
     * Получить инструктаж по ID.
     *
     * @param id уникальный идентификатор инструктажа.
     * @return найденный объект инструктажа.
     */
    Optional<Training> findOne(Long id);

    /**
     * Удалить инструктаж.
     *
     * @param id идентификатор удаляемого инструктажа.
     */
    void delete(Long id);
}
