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
import ru.georgdeveloper.myapp.domain.SafetyInstruction;
import ru.georgdeveloper.myapp.repository.SafetyInstructionRepository;
import ru.georgdeveloper.myapp.service.SafetyInstructionService;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST контроллер для управления инструкциями по технике безопасности.
 * Предоставляет CRUD-операции для сущности {@link ru.georgdeveloper.myapp.domain.SafetyInstruction}.
 */
@RestController
@RequestMapping("/api/safety-instructions")
public class SafetyInstructionResource {

    private static final Logger LOG = LoggerFactory.getLogger(SafetyInstructionResource.class);

    // Название сущности для сообщений об ошибках
    private static final String ENTITY_NAME = "safetyInstruction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SafetyInstructionService safetyInstructionService;
    private final SafetyInstructionRepository safetyInstructionRepository;

    /**
     * Конструктор контроллера.
     *
     * @param safetyInstructionService сервис для работы с инструкциями
     * @param safetyInstructionRepository репозиторий инструкций
     */
    public SafetyInstructionResource(
        SafetyInstructionService safetyInstructionService,
        SafetyInstructionRepository safetyInstructionRepository
    ) {
        this.safetyInstructionService = safetyInstructionService;
        this.safetyInstructionRepository = safetyInstructionRepository;
    }

    /**
     * Создает новую инструкцию по технике безопасности.
     * POST /api/safety-instructions
     *
     * @param safetyInstruction данные новой инструкции
     * @return ResponseEntity с созданной инструкцией или ошибкой 400 если ID уже существует
     * @throws URISyntaxException при некорректном URI
     */
    @PostMapping("")
    public ResponseEntity<SafetyInstruction> createSafetyInstruction(@Valid @RequestBody SafetyInstruction safetyInstruction)
        throws URISyntaxException {
        LOG.debug("Запрос на создание инструкции по ТБ: {}", safetyInstruction);
        if (safetyInstruction.getId() != null) {
            throw new BadRequestAlertException("Новая инструкция не может иметь ID", ENTITY_NAME, "idexists");
        }
        safetyInstruction = safetyInstructionService.save(safetyInstruction);
        return ResponseEntity.created(new URI("/api/safety-instructions/" + safetyInstruction.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, safetyInstruction.getId().toString()))
            .body(safetyInstruction);
    }

    /**
     * Полностью обновляет инструкцию по технике безопасности.
     * PUT /api/safety-instructions/{id}
     *
     * @param id ID инструкции
     * @param safetyInstruction обновленные данные инструкции
     * @return ResponseEntity с обновленной инструкцией или кодом ошибки
     * @throws URISyntaxException при некорректном URI
     */
    @PutMapping("/{id}")
    public ResponseEntity<SafetyInstruction> updateSafetyInstruction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SafetyInstruction safetyInstruction
    ) throws URISyntaxException {
        LOG.debug("Запрос на обновление инструкции по ТБ: ID {}, Данные: {}", id, safetyInstruction);
        if (safetyInstruction.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, safetyInstruction.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!safetyInstructionRepository.existsById(id)) {
            throw new BadRequestAlertException("Инструкция не найдена", ENTITY_NAME, "idnotfound");
        }

        safetyInstruction = safetyInstructionService.update(safetyInstruction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, safetyInstruction.getId().toString()))
            .body(safetyInstruction);
    }

    /**
     * Частично обновляет инструкцию по технике безопасности.
     * PATCH /api/safety-instructions/{id}
     *
     * @param id ID инструкции
     * @param safetyInstruction данные для частичного обновления
     * @return ResponseEntity с обновленной инструкцией или кодом ошибки
     * @throws URISyntaxException при некорректном URI
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SafetyInstruction> partialUpdateSafetyInstruction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SafetyInstruction safetyInstruction
    ) throws URISyntaxException {
        LOG.debug("Запрос на частичное обновление инструкции по ТБ: ID {}, Данные: {}", id, safetyInstruction);
        if (safetyInstruction.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, safetyInstruction.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!safetyInstructionRepository.existsById(id)) {
            throw new BadRequestAlertException("Инструкция не найдена", ENTITY_NAME, "idnotfound");
        }

        Optional<SafetyInstruction> result = safetyInstructionService.partialUpdate(safetyInstruction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, safetyInstruction.getId().toString())
        );
    }

    /**
     * Получает список инструкций по технике безопасности с пагинацией.
     * GET /api/safety-instructions
     *
     * @param pageable параметры пагинации
     * @return ResponseEntity со списком инструкций и заголовками пагинации
     */
    @GetMapping("")
    public ResponseEntity<List<SafetyInstruction>> getAllSafetyInstructions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("Запрос на получение списка инструкций по ТБ");
        Page<SafetyInstruction> page = safetyInstructionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Получает инструкцию по технике безопасности по ID.
     * GET /api/safety-instructions/{id}
     *
     * @param id ID инструкции
     * @return ResponseEntity с инструкцией или 404 если не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<SafetyInstruction> getSafetyInstruction(@PathVariable("id") Long id) {
        LOG.debug("Запрос на получение инструкции по ТБ: ID {}", id);
        Optional<SafetyInstruction> safetyInstruction = safetyInstructionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(safetyInstruction);
    }

    /**
     * Удаляет инструкцию по технике безопасности.
     * DELETE /api/safety-instructions/{id}
     *
     * @param id ID инструкции для удаления
     * @return ResponseEntity с кодом 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSafetyInstruction(@PathVariable("id") Long id) {
        LOG.debug("Запрос на удаление инструкции по ТБ: ID {}", id);
        safetyInstructionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
