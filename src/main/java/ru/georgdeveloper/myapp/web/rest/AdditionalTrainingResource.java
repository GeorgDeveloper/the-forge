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
import ru.georgdeveloper.myapp.domain.AdditionalTraining;
import ru.georgdeveloper.myapp.repository.AdditionalTrainingRepository;
import ru.georgdeveloper.myapp.service.AdditionalTrainingService;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST контроллер для управления {@link ru.georgdeveloper.myapp.domain.AdditionalTraining}.
 */
@RestController
@RequestMapping("/api/additional-trainings")
public class AdditionalTrainingResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdditionalTrainingResource.class);

    private static final String ENTITY_NAME = "additionalTraining";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdditionalTrainingService additionalTrainingService;
    private final AdditionalTrainingRepository additionalTrainingRepository;

    /**
     * Конструктор контроллера.
     */
    public AdditionalTrainingResource(
        AdditionalTrainingService additionalTrainingService,
        AdditionalTrainingRepository additionalTrainingRepository
    ) {
        this.additionalTrainingService = additionalTrainingService;
        this.additionalTrainingRepository = additionalTrainingRepository;
    }

    /**
     * Создает новое дополнительное обучение.
     * POST /additional-trainings
     *
     * @param additionalTraining данные для создания обучения
     * @return ResponseEntity с созданным обучением или ошибкой 400 если ID уже существует
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PostMapping("")
    public ResponseEntity<AdditionalTraining> createAdditionalTraining(@Valid @RequestBody AdditionalTraining additionalTraining)
        throws URISyntaxException {
        LOG.debug("REST запрос на создание AdditionalTraining: {}", additionalTraining);
        if (additionalTraining.getId() != null) {
            throw new BadRequestAlertException("Новое обучение не может иметь ID", ENTITY_NAME, "idexists");
        }
        additionalTraining = additionalTrainingService.save(additionalTraining);
        return ResponseEntity.created(new URI("/api/additional-trainings/" + additionalTraining.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, additionalTraining.getId().toString()))
            .body(additionalTraining);
    }

    /**
     * Полностью обновляет существующее дополнительное обучение.
     * PUT /additional-trainings/:id
     *
     * @param id ID обучения для обновления
     * @param additionalTraining новые данные обучения
     * @return ResponseEntity с обновленным обучением или кодом ошибки
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdditionalTraining> updateAdditionalTraining(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdditionalTraining additionalTraining
    ) throws URISyntaxException {
        LOG.debug("REST запрос на обновление AdditionalTraining: {}, {}", id, additionalTraining);
        if (additionalTraining.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, additionalTraining.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!additionalTrainingRepository.existsById(id)) {
            throw new BadRequestAlertException("Обучение не найдено", ENTITY_NAME, "idnotfound");
        }

        additionalTraining = additionalTrainingService.update(additionalTraining);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, additionalTraining.getId().toString()))
            .body(additionalTraining);
    }

    /**
     * Частично обновляет существующее дополнительное обучение.
     * PATCH /additional-trainings/:id
     *
     * @param id ID обучения для обновления
     * @param additionalTraining данные для частичного обновления
     * @return ResponseEntity с обновленным обучением или кодом ошибки
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdditionalTraining> partialUpdateAdditionalTraining(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdditionalTraining additionalTraining
    ) throws URISyntaxException {
        LOG.debug("REST запрос на частичное обновление AdditionalTraining: {}, {}", id, additionalTraining);
        if (additionalTraining.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, additionalTraining.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!additionalTrainingRepository.existsById(id)) {
            throw new BadRequestAlertException("Обучение не найдено", ENTITY_NAME, "idnotfound");
        }

        Optional<AdditionalTraining> result = additionalTrainingService.partialUpdate(additionalTraining);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, additionalTraining.getId().toString())
        );
    }

    /**
     * Получает список всех дополнительных обучений с пагинацией.
     * GET /additional-trainings
     *
     * @param pageable параметры пагинации
     * @return ResponseEntity со списком обучений
     */
    @GetMapping("")
    public ResponseEntity<List<AdditionalTraining>> getAllAdditionalTrainings(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST запрос на получение страницы AdditionalTrainings");
        Page<AdditionalTraining> page = additionalTrainingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Получает дополнительное обучение по ID.
     * GET /additional-trainings/:id
     *
     * @param id ID обучения
     * @return ResponseEntity с найденным обучением или 404 если не найдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdditionalTraining> getAdditionalTraining(@PathVariable("id") Long id) {
        LOG.debug("REST запрос на получение AdditionalTraining: {}", id);
        Optional<AdditionalTraining> additionalTraining = additionalTrainingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(additionalTraining);
    }

    /**
     * Удаляет дополнительное обучение по ID.
     * DELETE /additional-trainings/:id
     *
     * @param id ID обучения для удаления
     * @return ResponseEntity с кодом 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdditionalTraining(@PathVariable("id") Long id) {
        LOG.debug("REST запрос на удаление AdditionalTraining: {}", id);
        additionalTrainingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
