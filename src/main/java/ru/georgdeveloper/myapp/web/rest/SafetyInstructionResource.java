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
 * REST controller for managing {@link ru.georgdeveloper.myapp.domain.SafetyInstruction}.
 */
@RestController
@RequestMapping("/api/safety-instructions")
public class SafetyInstructionResource {

    private static final Logger LOG = LoggerFactory.getLogger(SafetyInstructionResource.class);

    private static final String ENTITY_NAME = "safetyInstruction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SafetyInstructionService safetyInstructionService;

    private final SafetyInstructionRepository safetyInstructionRepository;

    public SafetyInstructionResource(
        SafetyInstructionService safetyInstructionService,
        SafetyInstructionRepository safetyInstructionRepository
    ) {
        this.safetyInstructionService = safetyInstructionService;
        this.safetyInstructionRepository = safetyInstructionRepository;
    }

    /**
     * {@code POST  /safety-instructions} : Create a new safetyInstruction.
     *
     * @param safetyInstruction the safetyInstruction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new safetyInstruction, or with status {@code 400 (Bad Request)} if the safetyInstruction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SafetyInstruction> createSafetyInstruction(@Valid @RequestBody SafetyInstruction safetyInstruction)
        throws URISyntaxException {
        LOG.debug("REST request to save SafetyInstruction : {}", safetyInstruction);
        if (safetyInstruction.getId() != null) {
            throw new BadRequestAlertException("A new safetyInstruction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        safetyInstruction = safetyInstructionService.save(safetyInstruction);
        return ResponseEntity.created(new URI("/api/safety-instructions/" + safetyInstruction.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, safetyInstruction.getId().toString()))
            .body(safetyInstruction);
    }

    /**
     * {@code PUT  /safety-instructions/:id} : Updates an existing safetyInstruction.
     *
     * @param id the id of the safetyInstruction to save.
     * @param safetyInstruction the safetyInstruction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated safetyInstruction,
     * or with status {@code 400 (Bad Request)} if the safetyInstruction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the safetyInstruction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SafetyInstruction> updateSafetyInstruction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SafetyInstruction safetyInstruction
    ) throws URISyntaxException {
        LOG.debug("REST request to update SafetyInstruction : {}, {}", id, safetyInstruction);
        if (safetyInstruction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, safetyInstruction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!safetyInstructionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        safetyInstruction = safetyInstructionService.update(safetyInstruction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, safetyInstruction.getId().toString()))
            .body(safetyInstruction);
    }

    /**
     * {@code PATCH  /safety-instructions/:id} : Partial updates given fields of an existing safetyInstruction, field will ignore if it is null
     *
     * @param id the id of the safetyInstruction to save.
     * @param safetyInstruction the safetyInstruction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated safetyInstruction,
     * or with status {@code 400 (Bad Request)} if the safetyInstruction is not valid,
     * or with status {@code 404 (Not Found)} if the safetyInstruction is not found,
     * or with status {@code 500 (Internal Server Error)} if the safetyInstruction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SafetyInstruction> partialUpdateSafetyInstruction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SafetyInstruction safetyInstruction
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SafetyInstruction partially : {}, {}", id, safetyInstruction);
        if (safetyInstruction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, safetyInstruction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!safetyInstructionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SafetyInstruction> result = safetyInstructionService.partialUpdate(safetyInstruction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, safetyInstruction.getId().toString())
        );
    }

    /**
     * {@code GET  /safety-instructions} : get all the safetyInstructions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of safetyInstructions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SafetyInstruction>> getAllSafetyInstructions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of SafetyInstructions");
        Page<SafetyInstruction> page = safetyInstructionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /safety-instructions/:id} : get the "id" safetyInstruction.
     *
     * @param id the id of the safetyInstruction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the safetyInstruction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SafetyInstruction> getSafetyInstruction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SafetyInstruction : {}", id);
        Optional<SafetyInstruction> safetyInstruction = safetyInstructionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(safetyInstruction);
    }

    /**
     * {@code DELETE  /safety-instructions/:id} : delete the "id" safetyInstruction.
     *
     * @param id the id of the safetyInstruction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSafetyInstruction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SafetyInstruction : {}", id);
        safetyInstructionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
