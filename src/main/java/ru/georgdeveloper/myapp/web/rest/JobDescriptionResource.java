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
 * REST controller for managing {@link ru.georgdeveloper.myapp.domain.JobDescription}.
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

    public JobDescriptionResource(JobDescriptionService jobDescriptionService, JobDescriptionRepository jobDescriptionRepository) {
        this.jobDescriptionService = jobDescriptionService;
        this.jobDescriptionRepository = jobDescriptionRepository;
    }

    /**
     * {@code POST  /job-descriptions} : Create a new jobDescription.
     *
     * @param jobDescription the jobDescription to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobDescription, or with status {@code 400 (Bad Request)} if the jobDescription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<JobDescription> createJobDescription(@Valid @RequestBody JobDescription jobDescription)
        throws URISyntaxException {
        LOG.debug("REST request to save JobDescription : {}", jobDescription);
        if (jobDescription.getId() != null) {
            throw new BadRequestAlertException("A new jobDescription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        jobDescription = jobDescriptionService.save(jobDescription);
        return ResponseEntity.created(new URI("/api/job-descriptions/" + jobDescription.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, jobDescription.getId().toString()))
            .body(jobDescription);
    }

    /**
     * {@code PUT  /job-descriptions/:id} : Updates an existing jobDescription.
     *
     * @param id the id of the jobDescription to save.
     * @param jobDescription the jobDescription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobDescription,
     * or with status {@code 400 (Bad Request)} if the jobDescription is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobDescription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobDescription> updateJobDescription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JobDescription jobDescription
    ) throws URISyntaxException {
        LOG.debug("REST request to update JobDescription : {}, {}", id, jobDescription);
        if (jobDescription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobDescription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobDescriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        jobDescription = jobDescriptionService.update(jobDescription);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobDescription.getId().toString()))
            .body(jobDescription);
    }

    /**
     * {@code PATCH  /job-descriptions/:id} : Partial updates given fields of an existing jobDescription, field will ignore if it is null
     *
     * @param id the id of the jobDescription to save.
     * @param jobDescription the jobDescription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobDescription,
     * or with status {@code 400 (Bad Request)} if the jobDescription is not valid,
     * or with status {@code 404 (Not Found)} if the jobDescription is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobDescription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JobDescription> partialUpdateJobDescription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JobDescription jobDescription
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update JobDescription partially : {}, {}", id, jobDescription);
        if (jobDescription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobDescription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobDescriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JobDescription> result = jobDescriptionService.partialUpdate(jobDescription);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobDescription.getId().toString())
        );
    }

    /**
     * {@code GET  /job-descriptions} : get all the jobDescriptions.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobDescriptions in body.
     */
    @GetMapping("")
    public List<JobDescription> getAllJobDescriptions(@RequestParam(name = "filter", required = false) String filter) {
        if ("position-is-null".equals(filter)) {
            LOG.debug("REST request to get all JobDescriptions where position is null");
            return jobDescriptionService.findAllWherePositionIsNull();
        }
        LOG.debug("REST request to get all JobDescriptions");
        return jobDescriptionService.findAll();
    }

    /**
     * {@code GET  /job-descriptions/:id} : get the "id" jobDescription.
     *
     * @param id the id of the jobDescription to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobDescription, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobDescription> getJobDescription(@PathVariable("id") Long id) {
        LOG.debug("REST request to get JobDescription : {}", id);
        Optional<JobDescription> jobDescription = jobDescriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobDescription);
    }

    /**
     * {@code DELETE  /job-descriptions/:id} : delete the "id" jobDescription.
     *
     * @param id the id of the jobDescription to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobDescription(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete JobDescription : {}", id);
        jobDescriptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
