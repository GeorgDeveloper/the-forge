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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.georgdeveloper.myapp.domain.ProcedureDocument;
import ru.georgdeveloper.myapp.repository.ProcedureDocumentRepository;
import ru.georgdeveloper.myapp.service.ProcedureDocumentService;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST контроллер для управления процедурами и документациями.
 * Предоставляет CRUD-операции для сущности {@link ru.georgdeveloper.myapp.domain.ProcedureDocument}.
 */
@RestController
@RequestMapping("/api/procedure-documents")
public class ProcedureDocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProcedureDocumentResource.class);

    private static final String ENTITY_NAME = "procedureDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcedureDocumentService procedureDocumentService;
    private final ProcedureDocumentRepository procedureDocumentRepository;

    public ProcedureDocumentResource(
        ProcedureDocumentService procedureDocumentService,
        ProcedureDocumentRepository procedureDocumentRepository
    ) {
        this.procedureDocumentService = procedureDocumentService;
        this.procedureDocumentRepository = procedureDocumentRepository;
    }

    /**
     * Создает новую процедуру и документацию.
     * POST /api/procedure-documents
     */
    @PostMapping("")
    public ResponseEntity<ProcedureDocument> createProcedureDocument(@Valid @RequestBody ProcedureDocument procedureDocument)
        throws URISyntaxException {
        LOG.debug("Запрос на создание процедуры и документации: {}", procedureDocument);
        if (procedureDocument.getId() != null) {
            throw new BadRequestAlertException("Новая процедура и документация не может иметь ID", ENTITY_NAME, "idexists");
        }
        procedureDocument = procedureDocumentService.save(procedureDocument);
        return ResponseEntity.created(new URI("/api/procedure-documents/" + procedureDocument.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, procedureDocument.getId().toString()))
            .body(procedureDocument);
    }

    /**
     * Полностью обновляет процедуру и документацию.
     * PUT /api/procedure-documents/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProcedureDocument> updateProcedureDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProcedureDocument procedureDocument
    ) throws URISyntaxException {
        LOG.debug("Запрос на обновление процедуры и документации: ID {}, Данные: {}", id, procedureDocument);
        if (procedureDocument.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, procedureDocument.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!procedureDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Процедура и документация не найдена", ENTITY_NAME, "idnotfound");
        }

        procedureDocument = procedureDocumentService.update(procedureDocument);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, procedureDocument.getId().toString()))
            .body(procedureDocument);
    }

    /**
     * Частично обновляет процедуру и документацию.
     * PATCH /api/procedure-documents/{id}
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcedureDocument> partialUpdateProcedureDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProcedureDocument procedureDocument
    ) throws URISyntaxException {
        LOG.debug("Запрос на частичное обновление процедуры и документации: ID {}, Данные: {}", id, procedureDocument);
        if (procedureDocument.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, procedureDocument.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!procedureDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Процедура и документация не найдена", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcedureDocument> result = procedureDocumentService.partialUpdate(procedureDocument);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, procedureDocument.getId().toString())
        );
    }

    /**
     * Получает список процедур и документаций с пагинацией.
     * GET /api/procedure-documents
     */
    @GetMapping("")
    public ResponseEntity<List<ProcedureDocument>> getAllProcedureDocuments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("Запрос на получение списка процедур и документаций");
        Page<ProcedureDocument> page = procedureDocumentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Получает процедуру и документацию по ID.
     * GET /api/procedure-documents/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProcedureDocument> getProcedureDocument(@PathVariable("id") Long id) {
        LOG.debug("Запрос на получение процедуры и документации: ID {}", id);
        Optional<ProcedureDocument> procedureDocument = procedureDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(procedureDocument);
    }

    /**
     * Загружает PDF-файл для процедуры и документации.
     * POST /api/procedure-documents/{id}/pdf
     */
    @PostMapping(value = "/{id}/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProcedureDocument> uploadPdfFile(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        LOG.info("=== НАЧАЛО ЗАГРУЗКИ PDF ===");
        LOG.info("ID процедуры и документации: {}", id);
        LOG.info("Имя файла: {}", file.getOriginalFilename());
        LOG.info("Размер файла: {} bytes ({} MB)", file.getSize(), file.getSize() / (1024.0 * 1024.0));
        LOG.info("Тип контента: {}", file.getContentType());
        LOG.info("Пустое: {}", file.isEmpty());

        if (file.isEmpty()) {
            throw new BadRequestAlertException("Файл не может быть пустым", ENTITY_NAME, "fileempty");
        }

        if (!"application/pdf".equals(file.getContentType())) {
            throw new BadRequestAlertException("Поддерживаются только PDF-файлы", ENTITY_NAME, "invalidfiletype");
        }

        // Проверка размера файла (50MB)
        long maxSize = 50 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new BadRequestAlertException("Размер файла не должен превышать 50MB", ENTITY_NAME, "filesizetoolarge");
        }

        Optional<ProcedureDocument> procedureDocumentOpt = procedureDocumentService.findOne(id);
        if (procedureDocumentOpt.isEmpty()) {
            throw new BadRequestAlertException("Процедура и документация не найдена", ENTITY_NAME, "idnotfound");
        }

        ProcedureDocument procedureDocument = procedureDocumentOpt.get();
        try {
            procedureDocument.setPdfFileName(file.getOriginalFilename());
            procedureDocument.setPdfFileContentType(file.getContentType());
            procedureDocument.setPdfFile(file.getBytes());

            procedureDocument = procedureDocumentService.update(procedureDocument);

            LOG.info("PDF-файл успешно загружен для процедуры и документации ID {}: {}", id, file.getOriginalFilename());

            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .body(procedureDocument);
        } catch (Exception e) {
            LOG.error("Ошибка при загрузке PDF-файла: {}", e.getMessage(), e);
            throw new BadRequestAlertException("Ошибка при загрузке файла: " + e.getMessage(), ENTITY_NAME, "uploaderror");
        }
    }

    /**
     * Скачивает PDF-файл процедуры и документации.
     * GET /api/procedure-documents/{id}/pdf
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdfFile(@PathVariable("id") Long id) {
        LOG.debug("Запрос на скачивание PDF-файла процедуры и документации: ID {}", id);

        Optional<ProcedureDocument> procedureDocumentOpt = procedureDocumentService.findOne(id);
        if (procedureDocumentOpt.isEmpty()) {
            throw new BadRequestAlertException("Процедура и документация не найдена", ENTITY_NAME, "idnotfound");
        }

        ProcedureDocument procedureDocument = procedureDocumentOpt.get();
        if (procedureDocument.getPdfFile() == null) {
            throw new BadRequestAlertException("PDF-файл не найден", ENTITY_NAME, "filenotfound");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(procedureDocument.getPdfFileContentType()));
        headers.setContentDispositionFormData("attachment", procedureDocument.getPdfFileName());

        return ResponseEntity.ok().headers(headers).body(procedureDocument.getPdfFile());
    }

    /**
     * Удаляет PDF-файл процедуры и документации.
     * DELETE /api/procedure-documents/{id}/pdf
     */
    @DeleteMapping("/{id}/pdf")
    public ResponseEntity<ProcedureDocument> deletePdfFile(@PathVariable("id") Long id) {
        LOG.debug("Запрос на удаление PDF-файла процедуры и документации: ID {}", id);

        Optional<ProcedureDocument> procedureDocumentOpt = procedureDocumentService.findOne(id);
        if (procedureDocumentOpt.isEmpty()) {
            throw new BadRequestAlertException("Процедура и документация не найдена", ENTITY_NAME, "idnotfound");
        }

        ProcedureDocument procedureDocument = procedureDocumentOpt.get();
        procedureDocument.setPdfFileName(null);
        procedureDocument.setPdfFileContentType(null);
        procedureDocument.setPdfFile(null);

        procedureDocument = procedureDocumentService.update(procedureDocument);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(procedureDocument);
    }

    /**
     * Удаляет процедуру и документацию.
     * DELETE /api/procedure-documents/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcedureDocument(@PathVariable("id") Long id) {
        LOG.debug("Запрос на удаление процедуры и документации: ID {}", id);
        procedureDocumentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
