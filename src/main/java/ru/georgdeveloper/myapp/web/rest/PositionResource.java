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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.georgdeveloper.myapp.domain.Position;
import ru.georgdeveloper.myapp.repository.PositionRepository;
import ru.georgdeveloper.myapp.service.PositionService;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST контроллер для управления должностями {@link ru.georgdeveloper.myapp.domain.Position}.
 */
@RestController
@RequestMapping("/api/positions")
public class PositionResource {

    private static final Logger LOG = LoggerFactory.getLogger(PositionResource.class);

    private static final String ENTITY_NAME = "position";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PositionService positionService;
    private final PositionRepository positionRepository;

    /**
     * Конструктор контроллера.
     *
     * @param positionService сервис для работы с должностями
     * @param positionRepository репозиторий должностей
     */
    public PositionResource(PositionService positionService, PositionRepository positionRepository) {
        this.positionService = positionService;
        this.positionRepository = positionRepository;
    }

    /**
     * Создает новую должность.
     * POST /api/positions
     *
     * @param position данные новой должности
     * @return ResponseEntity с созданной должностью или ошибкой 400 если ID уже существует
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PostMapping("")
    public ResponseEntity<Position> createPosition(@Valid @RequestBody Position position) throws URISyntaxException {
        LOG.debug("REST запрос на создание должности: {}", position);
        if (position.getId() != null) {
            throw new BadRequestAlertException("Новая должность не может иметь ID", ENTITY_NAME, "idexists");
        }
        try {
            position = positionService.save(position);
        } catch (DataIntegrityViolationException e) {
            final String message = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : e.getMessage();
            if (
                message != null &&
                (message.contains("ux_position__job_description_id") ||
                    message.contains("duplicate key value") ||
                    message.contains("23505"))
            ) {
                throw new BadRequestAlertException(
                    "Данная должностная инструкция уже закреплена за другой должностью",
                    ENTITY_NAME,
                    "jobDescriptionInUse"
                );
            }
            throw e;
        }
        return ResponseEntity.created(new URI("/api/positions/" + position.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, position.getId().toString()))
            .body(position);
    }

    /**
     * Полностью обновляет данные должности.
     * PUT /api/positions/{id}
     *
     * @param id ID должности для обновления
     * @param position новые данные должности
     * @return ResponseEntity с обновленной должностью или кодом ошибки
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PutMapping("/{id}")
    public ResponseEntity<Position> updatePosition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Position position
    ) throws URISyntaxException {
        LOG.debug("REST запрос на обновление должности: {}, {}", id, position);
        if (position.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, position.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionRepository.existsById(id)) {
            throw new BadRequestAlertException("Должность не найдена", ENTITY_NAME, "idnotfound");
        }

        try {
            position = positionService.update(position);
        } catch (DataIntegrityViolationException e) {
            final String message = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : e.getMessage();
            if (
                message != null &&
                (message.contains("ux_position__job_description_id") ||
                    message.contains("duplicate key value") ||
                    message.contains("23505"))
            ) {
                throw new BadRequestAlertException(
                    "Данная должностная инструкция уже закреплена за другой должностью",
                    ENTITY_NAME,
                    "jobDescriptionInUse"
                );
            }
            throw e;
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, position.getId().toString()))
            .body(position);
    }

    /**
     * Частично обновляет данные должности.
     * PATCH /api/positions/{id}
     *
     * @param id ID должности для обновления
     * @param position данные для частичного обновления
     * @return ResponseEntity с обновленной должностью или кодом ошибки
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Position> partialUpdatePosition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Position position
    ) throws URISyntaxException {
        LOG.debug("REST запрос на частичное обновление должности: {}, {}", id, position);
        if (position.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, position.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionRepository.existsById(id)) {
            throw new BadRequestAlertException("Должность не найдена", ENTITY_NAME, "idnotfound");
        }

        Optional<Position> result = positionService.partialUpdate(position);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, position.getId().toString())
        );
    }

    /**
     * Получает список должностей с пагинацией.
     * GET /api/positions
     *
     * @param pageable параметры пагинации
     * @return ResponseEntity со списком должностей
     */
    @GetMapping("")
    public ResponseEntity<List<Position>> getAllPositions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST запрос на получение страницы должностей");
        Page<Position> page = positionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Получает должность по ID.
     * GET /api/positions/{id}
     *
     * @param id ID должности
     * @return ResponseEntity с найденной должностью или 404 если не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<Position> getPosition(@PathVariable("id") Long id) {
        LOG.debug("REST запрос на получение должности: {}", id);
        Optional<Position> position = positionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(position);
    }

    /**
     * Удаляет должность по ID.
     * DELETE /api/positions/{id}
     *
     * @param id ID должности для удаления
     * @return ResponseEntity с кодом 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable("id") Long id) {
        LOG.debug("REST запрос на удаление должности: {}", id);
        positionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
