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
import ru.georgdeveloper.myapp.domain.Training;
import ru.georgdeveloper.myapp.repository.TrainingRepository;
import ru.georgdeveloper.myapp.service.TrainingService;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST контроллер для управления сущностью {@link ru.georgdeveloper.myapp.domain.Training}.
 * Предоставляет CRUD операции для тренировок через HTTP API.
 */
@RestController
@RequestMapping("/api/trainings")
public class TrainingResource {

    // Логгер для записи информации о работе контроллера
    private static final Logger LOG = LoggerFactory.getLogger(TrainingResource.class);

    // Название сущности для использования в сообщениях и заголовках
    private static final String ENTITY_NAME = "training";

    // Название клиентского приложения из конфигурации
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    // Сервис для работы с тренировками
    private final TrainingService trainingService;

    // Репозиторий для работы с данными тренировок
    private final TrainingRepository trainingRepository;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param trainingService сервис тренировок
     * @param trainingRepository репозиторий тренировок
     */
    public TrainingResource(TrainingService trainingService, TrainingRepository trainingRepository) {
        this.trainingService = trainingService;
        this.trainingRepository = trainingRepository;
    }

    /**
     * {@code POST  /trainings} : Создает новую тренировку.
     *
     * @param training тренировка для создания (передается в теле запроса)
     * @return ответ со статусом {@code 201 (Created)} и с телом созданной тренировки,
     *         или статус {@code 400 (Bad Request)} если у тренировки уже есть ID.
     * @throws URISyntaxException если синтаксис URI местоположения некорректен.
     */
    @PostMapping("")
    public ResponseEntity<Training> createTraining(@Valid @RequestBody Training training) throws URISyntaxException {
        LOG.debug("REST запрос для сохранения Training : {}", training);
        if (training.getId() != null) {
            throw new BadRequestAlertException("Новая тренировка не может иметь ID", ENTITY_NAME, "idexists");
        }
        training = trainingService.save(training);
        return ResponseEntity.created(new URI("/api/trainings/" + training.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, training.getId().toString()))
            .body(training);
    }

    /**
     * {@code PUT  /trainings/:id} : Полностью обновляет существующую тренировку.
     *
     * @param id ID тренировки для обновления
     * @param training новые данные тренировки (передаются в теле запроса)
     * @return ответ со статусом {@code 200 (OK)} и с телом обновленной тренировки,
     *         или статус {@code 400 (Bad Request)} если данные невалидны,
     *         или статус {@code 500 (Internal Server Error)} если не удалось обновить.
     * @throws URISyntaxException если синтаксис URI местоположения некорректен.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Training> updateTraining(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Training training
    ) throws URISyntaxException {
        LOG.debug("REST запрос для обновления Training : {}, {}", id, training);
        if (training.getId() == null) {
            throw new BadRequestAlertException("Неверный id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, training.getId())) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingRepository.existsById(id)) {
            throw new BadRequestAlertException("Сущность не найдена", ENTITY_NAME, "idnotfound");
        }

        training = trainingService.update(training);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, training.getId().toString()))
            .body(training);
    }

    /**
     * {@code PATCH  /trainings/:id} : Частично обновляет поля существующей тренировки.
     * Обновляются только переданные поля, null значения игнорируются.
     *
     * @param id ID тренировки для обновления
     * @param training данные для частичного обновления (передаются в теле запроса)
     * @return ответ со статусом {@code 200 (OK)} и с телом обновленной тренировки,
     *         или статус {@code 400 (Bad Request)} если данные невалидны,
     *         или статус {@code 404 (Not Found)} если тренировка не найдена,
     *         или статус {@code 500 (Internal Server Error)} если не удалось обновить.
     * @throws URISyntaxException если синтаксис URI местоположения некорректен.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Training> partialUpdateTraining(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Training training
    ) throws URISyntaxException {
        LOG.debug("REST запрос для частичного обновления Training : {}, {}", id, training);
        if (training.getId() == null) {
            throw new BadRequestAlertException("Неверный id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, training.getId())) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingRepository.existsById(id)) {
            throw new BadRequestAlertException("Сущность не найдена", ENTITY_NAME, "idnotfound");
        }

        Optional<Training> result = trainingService.partialUpdate(training);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, training.getId().toString())
        );
    }

    /**
     * {@code GET  /trainings} : Получает список всех тренировок с пагинацией.
     *
     * @param pageable информация о пагинации (номер страницы, размер, сортировка)
     * @return ответ со статусом {@code 200 (OK)} и списком тренировок в теле ответа.
     */
    @GetMapping("")
    public ResponseEntity<List<Training>> getAllTrainings(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST запрос для получения страницы Trainings");
        Page<Training> page = trainingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trainings/:id} : Получает тренировку по ID.
     *
     * @param id ID тренировки для получения
     * @return ответ со статусом {@code 200 (OK)} и с телом тренировки,
     *         или статус {@code 404 (Not Found)} если тренировка не найдена.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Training> getTraining(@PathVariable("id") Long id) {
        LOG.debug("REST запрос для получения Training : {}", id);
        Optional<Training> training = trainingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(training);
    }

    /**
     * {@code DELETE  /trainings/:id} : Удаляет тренировку по ID.
     *
     * @param id ID тренировки для удаления
     * @return ответ со статусом {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable("id") Long id) {
        LOG.debug("REST запрос для удаления Training : {}", id);
        trainingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
