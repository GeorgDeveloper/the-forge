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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.georgdeveloper.myapp.domain.JobDescription;
import ru.georgdeveloper.myapp.repository.JobDescriptionRepository;
import ru.georgdeveloper.myapp.service.JobDescriptionService;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST контроллер для управления описаниями должностной инструкцией {@link ru.georgdeveloper.myapp.domain.JobDescription}.
 */
@RestController
@RequestMapping("/api/job-descriptions")
public class JobDescriptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(JobDescriptionResource.class);

    private static final String ENTITY_NAME = "jobDescription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobDescriptionService jobDescriptionService;
    private final JobDescriptionRepository jobDescriptionRepository;

    /**
     * Конструктор контроллера.
     *
     * @param jobDescriptionService сервис для работы с описаниями должностей
     * @param jobDescriptionRepository репозиторий описаний должностей
     */
    public JobDescriptionResource(JobDescriptionService jobDescriptionService, JobDescriptionRepository jobDescriptionRepository) {
        this.jobDescriptionService = jobDescriptionService;
        this.jobDescriptionRepository = jobDescriptionRepository;
    }

    /**
     * Создает новое описание должности.
     * POST /api/job-descriptions
     *
     * @param jobDescription данные для создания описания должности
     * @return ResponseEntity с созданным описанием или ошибкой 400 если ID уже существует
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PostMapping("")
    public ResponseEntity<JobDescription> createJobDescription(@Valid @RequestBody JobDescription jobDescription)
        throws URISyntaxException {
        LOG.debug("REST запрос на создание описания должности: {}", jobDescription);
        if (jobDescription.getId() != null) {
            throw new BadRequestAlertException("Новое описание должности не может иметь ID", ENTITY_NAME, "idexists");
        }
        jobDescription = jobDescriptionService.save(jobDescription);
        return ResponseEntity.created(new URI("/api/job-descriptions/" + jobDescription.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, jobDescription.getId().toString()))
            .body(jobDescription);
    }

    /**
     * Полностью обновляет описание должности.
     * PUT /api/job-descriptions/{id}
     *
     * @param id ID описания должности
     * @param jobDescription новые данные описания
     * @return ResponseEntity с обновленным описанием или кодом ошибки
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobDescription> updateJobDescription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JobDescription jobDescription
    ) throws URISyntaxException {
        LOG.debug("REST запрос на обновление описания должности: {}, {}", id, jobDescription);
        if (jobDescription.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobDescription.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobDescriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Описание должности не найдено", ENTITY_NAME, "idnotfound");
        }

        jobDescription = jobDescriptionService.update(jobDescription);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobDescription.getId().toString()))
            .body(jobDescription);
    }

    /**
     * Частично обновляет описание должности.
     * PATCH /api/job-descriptions/{id}
     *
     * @param id ID описания должности
     * @param jobDescription данные для частичного обновления
     * @return ResponseEntity с обновленным описанием или кодом ошибки
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JobDescription> partialUpdateJobDescription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JobDescription jobDescription
    ) throws URISyntaxException {
        LOG.debug("REST запрос на частичное обновление описания должности: {}, {}", id, jobDescription);
        if (jobDescription.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobDescription.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobDescriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Описание должности не найдено", ENTITY_NAME, "idnotfound");
        }

        Optional<JobDescription> result = jobDescriptionService.partialUpdate(jobDescription);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobDescription.getId().toString())
        );
    }

    /**
     * Получает список описаний должностей.
     * GET /api/job-descriptions
     *
     * @param filter фильтр для запроса (например "position-is-null")
     * @return список описаний должностей
     */
    @GetMapping("")
    public List<JobDescription> getAllJobDescriptions(@RequestParam(name = "filter", required = false) String filter) {
        if ("position-is-null".equals(filter)) {
            LOG.debug("REST запрос на получение описаний должностей без привязки к позиции");
            return jobDescriptionService.findAllWherePositionIsNull();
        }
        LOG.debug("REST запрос на получение всех описаний должностей");
        return jobDescriptionService.findAll();
    }

    /**
     * Получает описание должности по ID.
     * GET /api/job-descriptions/{id}
     *
     * @param id ID описания должности
     * @return ResponseEntity с найденным описанием или 404 если не найдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobDescription> getJobDescription(@PathVariable("id") Long id) {
        LOG.debug("REST запрос на получение описания должности: {}", id);
        Optional<JobDescription> jobDescription = jobDescriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobDescription);
    }

    /**
     * Удаляет описание должности по ID.
     * DELETE /api/job-descriptions/{id}
     *
     * @param id ID описания должности для удаления
     * @return ResponseEntity с кодом 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobDescription(@PathVariable("id") Long id) {
        LOG.debug("REST запрос на удаление описания должности: {}", id);
        jobDescriptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
