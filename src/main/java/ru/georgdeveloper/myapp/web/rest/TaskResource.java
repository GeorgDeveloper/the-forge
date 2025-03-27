package ru.georgdeveloper.myapp.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.georgdeveloper.myapp.domain.Task;
import ru.georgdeveloper.myapp.repository.TaskRepository;
import ru.georgdeveloper.myapp.service.TaskService;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST контроллер для управления задачами {@link ru.georgdeveloper.myapp.domain.Task}.
 * Предоставляет CRUD-операции для работы с задачами.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskResource {

    private static final Logger LOG = LoggerFactory.getLogger(TaskResource.class);

    // Название сущности для сообщений об ошибках
    private static final String ENTITY_NAME = "task";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    /**
     * Конструктор контроллера.
     *
     * @param taskService сервис для работы с задачами
     * @param taskRepository репозиторий задач
     */
    public TaskResource(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    /**
     * Создает новую задачу.
     * POST /api/tasks
     *
     * @param task данные новой задачи
     * @return ResponseEntity с созданной задачей или ошибкой 400 если ID уже существует
     * @throws URISyntaxException при некорректном URI
     */
    @PostMapping("")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) throws URISyntaxException {
        LOG.debug("Запрос на создание задачи: {}", task);
        if (task.getId() != null) {
            throw new BadRequestAlertException("Новая задача не может иметь ID", ENTITY_NAME, "idexists");
        }
        task = taskService.save(task);
        return ResponseEntity.created(new URI("/api/tasks/" + task.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, task.getId().toString()))
            .body(task);
    }

    /**
     * Полностью обновляет задачу.
     * PUT /api/tasks/{id}
     *
     * @param id ID задачи
     * @param task обновленные данные задачи
     * @return ResponseEntity с обновленной задачей или кодом ошибки
     * @throws URISyntaxException при некорректном URI
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Task task)
        throws URISyntaxException {
        LOG.debug("Запрос на обновление задачи: ID {}, Данные: {}", id, task);
        if (task.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, task.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskRepository.existsById(id)) {
            throw new BadRequestAlertException("Задача не найдена", ENTITY_NAME, "idnotfound");
        }

        task = taskService.update(task);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, task.getId().toString()))
            .body(task);
    }

    /**
     * Частично обновляет задачу.
     * PATCH /api/tasks/{id}
     *
     * @param id ID задачи
     * @param task данные для частичного обновления
     * @return ResponseEntity с обновленной задачей или кодом ошибки
     * @throws URISyntaxException при некорректном URI
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Task> partialUpdateTask(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Task task
    ) throws URISyntaxException {
        LOG.debug("Запрос на частичное обновление задачи: ID {}, Данные: {}", id, task);
        if (task.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, task.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskRepository.existsById(id)) {
            throw new BadRequestAlertException("Задача не найдена", ENTITY_NAME, "idnotfound");
        }

        Optional<Task> result = taskService.partialUpdate(task);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, task.getId().toString())
        );
    }

    /**
     * Получает список задач с пагинацией.
     * GET /api/tasks
     *
     * @param pageable параметры пагинации
     * @return ResponseEntity со списком задач и заголовками пагинации
     */
    @GetMapping("")
    public ResponseEntity<List<Task>> getAllTasks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("Запрос на получение списка задач");
        Page<Task> page = taskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Получает задачу по ID.
     * GET /api/tasks/{id}
     *
     * @param id ID задачи
     * @return ResponseEntity с задачей или 404 если не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") Long id) {
        LOG.debug("Запрос на получение задачи: ID {}", id);
        Optional<Task> task = taskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(task);
    }

    /**
     * Удаляет задачу.
     * DELETE /api/tasks/{id}
     *
     * @param id ID задачи для удаления
     * @return ResponseEntity с кодом 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        LOG.debug("Запрос на удаление задачи: ID {}", id);
        taskService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
