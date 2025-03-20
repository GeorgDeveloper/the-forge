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
 * REST controller for managing {@link ru.georgdeveloper.myapp.domain.AdditionalTraining}.
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

    public AdditionalTrainingResource(
        AdditionalTrainingService additionalTrainingService,
        AdditionalTrainingRepository additionalTrainingRepository
    ) {
        this.additionalTrainingService = additionalTrainingService;
        this.additionalTrainingRepository = additionalTrainingRepository;
    }

    /**
     * {@code POST  /additional-trainings} : Create a new additionalTraining.
     *
     * @param additionalTraining the additionalTraining to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new additionalTraining, or with status {@code 400 (Bad Request)} if the additionalTraining has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AdditionalTraining> createAdditionalTraining(@Valid @RequestBody AdditionalTraining additionalTraining)
        throws URISyntaxException {
        LOG.debug("REST request to save AdditionalTraining : {}", additionalTraining);
        if (additionalTraining.getId() != null) {
            throw new BadRequestAlertException("A new additionalTraining cannot already have an ID", ENTITY_NAME, "idexists");
        }
        additionalTraining = additionalTrainingService.save(additionalTraining);
        return ResponseEntity.created(new URI("/api/additional-trainings/" + additionalTraining.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, additionalTraining.getId().toString()))
            .body(additionalTraining);
    }

    /**
     * {@code PUT  /additional-trainings/:id} : Updates an existing additionalTraining.
     *
     * @param id the id of the additionalTraining to save.
     * @param additionalTraining the additionalTraining to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated additionalTraining,
     * or with status {@code 400 (Bad Request)} if the additionalTraining is not valid,
     * or with status {@code 500 (Internal Server Error)} if the additionalTraining couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdditionalTraining> updateAdditionalTraining(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdditionalTraining additionalTraining
    ) throws URISyntaxException {
        LOG.debug("REST request to update AdditionalTraining : {}, {}", id, additionalTraining);
        if (additionalTraining.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, additionalTraining.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!additionalTrainingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        additionalTraining = additionalTrainingService.update(additionalTraining);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, additionalTraining.getId().toString()))
            .body(additionalTraining);
    }

    /**
     * {@code PATCH  /additional-trainings/:id} : Partial updates given fields of an existing additionalTraining, field will ignore if it is null
     *
     * @param id the id of the additionalTraining to save.
     * @param additionalTraining the additionalTraining to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated additionalTraining,
     * or with status {@code 400 (Bad Request)} if the additionalTraining is not valid,
     * or with status {@code 404 (Not Found)} if the additionalTraining is not found,
     * or with status {@code 500 (Internal Server Error)} if the additionalTraining couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdditionalTraining> partialUpdateAdditionalTraining(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdditionalTraining additionalTraining
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AdditionalTraining partially : {}, {}", id, additionalTraining);
        if (additionalTraining.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, additionalTraining.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!additionalTrainingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdditionalTraining> result = additionalTrainingService.partialUpdate(additionalTraining);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, additionalTraining.getId().toString())
        );
    }

    /**
     * {@code GET  /additional-trainings} : get all the additionalTrainings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of additionalTrainings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AdditionalTraining>> getAllAdditionalTrainings(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of AdditionalTrainings");
        Page<AdditionalTraining> page = additionalTrainingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /additional-trainings/:id} : get the "id" additionalTraining.
     *
     * @param id the id of the additionalTraining to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the additionalTraining, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdditionalTraining> getAdditionalTraining(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AdditionalTraining : {}", id);
        Optional<AdditionalTraining> additionalTraining = additionalTrainingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(additionalTraining);
    }

    /**
     * {@code DELETE  /additional-trainings/:id} : delete the "id" additionalTraining.
     *
     * @param id the id of the additionalTraining to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdditionalTraining(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AdditionalTraining : {}", id);
        additionalTrainingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
