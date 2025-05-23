package ru.georgdeveloper.myapp.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Task;
import ru.georgdeveloper.myapp.repository.TaskRepository;
import ru.georgdeveloper.myapp.service.TaskService;

/**
 * Реализация сервиса для управления задачами.
 * Обеспечивает CRUD-операции для сущности {@link Task}.
 */
@Service // Указывает, что класс является Spring-сервисом
@Transactional // Все методы выполняются в транзакционном контексте
public class TaskServiceImpl implements TaskService {

    // Логгер для записи информации о работе сервиса
    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    // Репозиторий для работы с задачами в БД
    private final TaskRepository taskRepository;

    /**
     * Конструктор с внедрением зависимости TaskRepository
     * @param taskRepository - репозиторий для работы с задачами
     */
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Сохраняет новую задачу
     * @param task - сущность задачи для сохранения
     * @return сохраненная задача
     */
    @Override
    public Task save(Task task) {
        LOG.debug("Запрос на сохранение задачи: {}", task);
        return taskRepository.save(task);
    }

    /**
     * Обновляет существующую задачу
     * @param task - сущность с обновленными данными задачи
     * @return обновленная задача
     */
    @Override
    public Task update(Task task) {
        LOG.debug("Запрос на обновление задачи: {}", task);
        return taskRepository.save(task);
    }

    /**
     * Частично обновляет задачу (только указанные поля)
     * @param task - сущность с полями для обновления
     * @return Optional с обновленной задачей, если задача найдена
     */
    @Override
    public Optional<Task> partialUpdate(Task task) {
        LOG.debug("Запрос на частичное обновление задачи: {}", task);

        return taskRepository
            .findById(task.getId())
            .map(existingTask -> {
                // Обновляем только те поля, которые явно указаны
                if (task.getTaskName() != null) {
                    existingTask.setTaskName(task.getTaskName());
                }
                if (task.getCreationDate() != null) {
                    existingTask.setCreationDate(task.getCreationDate());
                }
                if (task.getPlannedCompletionDate() != null) {
                    existingTask.setPlannedCompletionDate(task.getPlannedCompletionDate());
                }
                if (task.getStatus() != null) {
                    existingTask.setStatus(task.getStatus());
                }
                if (task.getPriority() != null) {
                    existingTask.setPriority(task.getPriority());
                }
                if (task.getBody() != null) {
                    existingTask.setBody(task.getBody());
                }

                return existingTask;
            })
            .map(taskRepository::save);
    }

    /**
     * Получает список всех задач с пагинацией
     * @param pageable - параметры пагинации
     * @return страница с задачами
     */
    @Override
    @Transactional(readOnly = true) // Оптимизация для операций чтения
    public Page<Task> findAll(Pageable pageable) {
        LOG.debug("Запрос на получение всех задач");
        return taskRepository.findAll(pageable);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    /**
     * Находит задачу по ID
     * @param id - идентификатор задачи
     * @return Optional с найденной задачей или пустой
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Task> findOne(Long id) {
        LOG.debug("Запрос на получение задачи по ID: {}", id);
        return taskRepository.findById(id);
    }

    /**
     * Удаляет задачу по ID
     * @param id - идентификатор задачи для удаления
     */
    @Override
    public void delete(Long id) {
        LOG.debug("Запрос на удаление задачи с ID: {}", id);
        taskRepository.deleteById(id);
    }
}
