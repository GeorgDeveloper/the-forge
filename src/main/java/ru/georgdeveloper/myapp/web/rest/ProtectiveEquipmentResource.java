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
 * REST controller for managing {@link ru.georgdeveloper.myapp.domain.ProtectiveEquipment}.
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

    public ProtectiveEquipmentResource(
        ProtectiveEquipmentService protectiveEquipmentService,
        ProtectiveEquipmentRepository protectiveEquipmentRepository
    ) {
        this.protectiveEquipmentService = protectiveEquipmentService;
        this.protectiveEquipmentRepository = protectiveEquipmentRepository;
    }

    /**
     * {@code POST  /protective-equipments} : Create a new protectiveEquipment.
     *
     * @param protectiveEquipment the protectiveEquipment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new protectiveEquipment, or with status {@code 400 (Bad Request)} if the protectiveEquipment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProtectiveEquipment> createProtectiveEquipment(@Valid @RequestBody ProtectiveEquipment protectiveEquipment)
        throws URISyntaxException {
        LOG.debug("REST request to save ProtectiveEquipment : {}", protectiveEquipment);
        if (protectiveEquipment.getId() != null) {
            throw new BadRequestAlertException("A new protectiveEquipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        protectiveEquipment = protectiveEquipmentService.save(protectiveEquipment);
        return ResponseEntity.created(new URI("/api/protective-equipments/" + protectiveEquipment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, protectiveEquipment.getId().toString()))
            .body(protectiveEquipment);
    }

    /**
     * {@code PUT  /protective-equipments/:id} : Updates an existing protectiveEquipment.
     *
     * @param id the id of the protectiveEquipment to save.
     * @param protectiveEquipment the protectiveEquipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protectiveEquipment,
     * or with status {@code 400 (Bad Request)} if the protectiveEquipment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the protectiveEquipment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProtectiveEquipment> updateProtectiveEquipment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProtectiveEquipment protectiveEquipment
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProtectiveEquipment : {}, {}", id, protectiveEquipment);
        if (protectiveEquipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, protectiveEquipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!protectiveEquipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        protectiveEquipment = protectiveEquipmentService.update(protectiveEquipment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, protectiveEquipment.getId().toString()))
            .body(protectiveEquipment);
    }

    /**
     * {@code PATCH  /protective-equipments/:id} : Partial updates given fields of an existing protectiveEquipment, field will ignore if it is null
     *
     * @param id the id of the protectiveEquipment to save.
     * @param protectiveEquipment the protectiveEquipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protectiveEquipment,
     * or with status {@code 400 (Bad Request)} if the protectiveEquipment is not valid,
     * or with status {@code 404 (Not Found)} if the protectiveEquipment is not found,
     * or with status {@code 500 (Internal Server Error)} if the protectiveEquipment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProtectiveEquipment> partialUpdateProtectiveEquipment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProtectiveEquipment protectiveEquipment
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProtectiveEquipment partially : {}, {}", id, protectiveEquipment);
        if (protectiveEquipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, protectiveEquipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!protectiveEquipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProtectiveEquipment> result = protectiveEquipmentService.partialUpdate(protectiveEquipment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, protectiveEquipment.getId().toString())
        );
    }

    /**
     * {@code GET  /protective-equipments} : get all the protectiveEquipments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of protectiveEquipments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProtectiveEquipment>> getAllProtectiveEquipments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ProtectiveEquipments");
        Page<ProtectiveEquipment> page = protectiveEquipmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /protective-equipments/:id} : get the "id" protectiveEquipment.
     *
     * @param id the id of the protectiveEquipment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the protectiveEquipment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProtectiveEquipment> getProtectiveEquipment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProtectiveEquipment : {}", id);
        Optional<ProtectiveEquipment> protectiveEquipment = protectiveEquipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(protectiveEquipment);
    }

    /**
     * {@code DELETE  /protective-equipments/:id} : delete the "id" protectiveEquipment.
     *
     * @param id the id of the protectiveEquipment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProtectiveEquipment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProtectiveEquipment : {}", id);
        protectiveEquipmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
