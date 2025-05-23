package ru.georgdeveloper.myapp.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.Task;

/**
 * Интерфейс сервиса для управления {@link ru.georgdeveloper.myapp.domain.Task}.
 */
public interface TaskService {
    /**
     * Сохранить задачу.
     *
     * @param task сохраняемая задача.
     * @return сохраненная задача.
     */
    Task save(Task task);

    /**
     * Обновить задачу.
     *
     * @param task обновляемая задача.
     * @return обновленная задача.
     */
    Task update(Task task);

    /**
     * Частично обновить задачу.
     *
     * @param task задача для частичного обновления.
     * @return обновленная задача.
     */
    Optional<Task> partialUpdate(Task task);

    /**
     * Получить все задачи.
     *
     * @param pageable параметры пагинации.
     * @return список задач.
     */
    Page<Task> findAll(Pageable pageable);

    /**
     * Получить все задачи.
     *
     * @return список задач.
     */
    List<Task> findAll();

    /**
     * Получить задачу по идентификатору.
     *
     * @param id идентификатор задачи.
     * @return найденная задача.
     */
    Optional<Task> findOne(Long id);

    /**
     * Удалить задачу.
     *
     * @param id идентификатор удаляемой задачи.
     */
    void delete(Long id);
}
