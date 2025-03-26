package ru.georgdeveloper.myapp.service;

import java.util.List;
import java.util.Optional;
import ru.georgdeveloper.myapp.domain.JobDescription;

/**
 * Интерфейс сервиса для управления {@link ru.georgdeveloper.myapp.domain.JobDescription}.
 */
public interface JobDescriptionService {
    /**
     * Сохранить описание должности.
     *
     * @param jobDescription сущность для сохранения.
     * @return сохранённая сущность.
     */
    JobDescription save(JobDescription jobDescription);

    /**
     * Обновить описание должности.
     *
     * @param jobDescription сущность для обновления.
     * @return обновлённая сущность.
     */
    JobDescription update(JobDescription jobDescription);

    /**
     * Частично обновить описание должности.
     *
     * @param jobDescription сущность для частичного обновления.
     * @return обновлённая сущность.
     */
    Optional<JobDescription> partialUpdate(JobDescription jobDescription);

    /**
     * Получить все описания должностей.
     *
     * @return список сущностей.
     */
    List<JobDescription> findAll();

    /**
     * Получить все описания должностей, где Position (Должность) равна {@code null}.
     *
     * @return {@link List} сущностей.
     */
    List<JobDescription> findAllWherePositionIsNull();

    /**
     * Получить описание должности по "id".
     *
     * @param id идентификатор сущности.
     * @return сущность.
     */
    Optional<JobDescription> findOne(Long id);

    /**
     * Удалить описание должности по "id".
     *
     * @param id идентификатор сущности.
     */
    void delete(Long id);
}
