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
import ru.georgdeveloper.myapp.domain.ProtectiveEquipment;
import ru.georgdeveloper.myapp.repository.ProtectiveEquipmentRepository;
import ru.georgdeveloper.myapp.service.ProtectiveEquipmentService;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST контроллер для управления средствами индивидуальной защиты (СИЗ) {@link ru.georgdeveloper.myapp.domain.ProtectiveEquipment}.
 */
@RestController
@RequestMapping("/api/protective-equipments")
public class ProtectiveEquipmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProtectiveEquipmentResource.class);

    private static final String ENTITY_NAME = "protectiveEquipment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProtectiveEquipmentService protectiveEquipmentService;
    private final ProtectiveEquipmentRepository protectiveEquipmentRepository;

    /**
     * Конструктор контроллера.
     *
     * @param protectiveEquipmentService сервис для работы с СИЗ
     * @param protectiveEquipmentRepository репозиторий СИЗ
     */
    public ProtectiveEquipmentResource(
        ProtectiveEquipmentService protectiveEquipmentService,
        ProtectiveEquipmentRepository protectiveEquipmentRepository
    ) {
        this.protectiveEquipmentService = protectiveEquipmentService;
        this.protectiveEquipmentRepository = protectiveEquipmentRepository;
    }

    /**
     * Создает новое средство индивидуальной защиты.
     * POST /api/protective-equipments
     *
     * @param protectiveEquipment данные нового СИЗ
     * @return ResponseEntity с созданным СИЗ или ошибкой 400 если ID уже существует
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PostMapping("")
    public ResponseEntity<ProtectiveEquipment> createProtectiveEquipment(@Valid @RequestBody ProtectiveEquipment protectiveEquipment)
        throws URISyntaxException {
        LOG.debug("REST запрос на создание СИЗ: {}", protectiveEquipment);
        if (protectiveEquipment.getId() != null) {
            throw new BadRequestAlertException("Новое средство защиты не может иметь ID", ENTITY_NAME, "idexists");
        }
        protectiveEquipment = protectiveEquipmentService.save(protectiveEquipment);
        return ResponseEntity.created(new URI("/api/protective-equipments/" + protectiveEquipment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, protectiveEquipment.getId().toString()))
            .body(protectiveEquipment);
    }

    /**
     * Полностью обновляет данные средства защиты.
     * PUT /api/protective-equipments/{id}
     *
     * @param id ID СИЗ для обновления
     * @param protectiveEquipment новые данные СИЗ
     * @return ResponseEntity с обновленным СИЗ или кодом ошибки
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProtectiveEquipment> updateProtectiveEquipment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProtectiveEquipment protectiveEquipment
    ) throws URISyntaxException {
        LOG.debug("REST запрос на обновление СИЗ: {}, {}", id, protectiveEquipment);
        if (protectiveEquipment.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, protectiveEquipment.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!protectiveEquipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Средство защиты не найдено", ENTITY_NAME, "idnotfound");
        }

        protectiveEquipment = protectiveEquipmentService.update(protectiveEquipment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, protectiveEquipment.getId().toString()))
            .body(protectiveEquipment);
    }

    /**
     * Частично обновляет данные средства защиты.
     * PATCH /api/protective-equipments/{id}
     *
     * @param id ID СИЗ для обновления
     * @param protectiveEquipment данные для частичного обновления
     * @return ResponseEntity с обновленным СИЗ или кодом ошибки
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProtectiveEquipment> partialUpdateProtectiveEquipment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProtectiveEquipment protectiveEquipment
    ) throws URISyntaxException {
        LOG.debug("REST запрос на частичное обновление СИЗ: {}, {}", id, protectiveEquipment);
        if (protectiveEquipment.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, protectiveEquipment.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!protectiveEquipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Средство защиты не найдено", ENTITY_NAME, "idnotfound");
        }

        Optional<ProtectiveEquipment> result = protectiveEquipmentService.partialUpdate(protectiveEquipment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, protectiveEquipment.getId().toString())
        );
    }

    /**
     * Получает список средств защиты с пагинацией.
     * GET /api/protective-equipments
     *
     * @param pageable параметры пагинации
     * @return ResponseEntity со списком СИЗ
     */
    @GetMapping("")
    public ResponseEntity<List<ProtectiveEquipment>> getAllProtectiveEquipments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST запрос на получение страницы средств защиты");
        Page<ProtectiveEquipment> page = protectiveEquipmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Получает средство защиты по ID.
     * GET /api/protective-equipments/{id}
     *
     * @param id ID средства защиты
     * @return ResponseEntity с найденным СИЗ или 404 если не найдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProtectiveEquipment> getProtectiveEquipment(@PathVariable("id") Long id) {
        LOG.debug("REST запрос на получение средства защиты: {}", id);
        Optional<ProtectiveEquipment> protectiveEquipment = protectiveEquipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(protectiveEquipment);
    }

    /**
     * Удаляет средство защиты по ID.
     * DELETE /api/protective-equipments/{id}
     *
     * @param id ID средства защиты для удаления
     * @return ResponseEntity с кодом 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProtectiveEquipment(@PathVariable("id") Long id) {
        LOG.debug("REST запрос на удаление средства защиты: {}", id);
        protectiveEquipmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
