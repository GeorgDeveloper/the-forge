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
import ru.georgdeveloper.myapp.domain.Position;
import ru.georgdeveloper.myapp.repository.PositionRepository;
import ru.georgdeveloper.myapp.service.PositionService;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.georgdeveloper.myapp.domain.Position}.
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

    public PositionResource(PositionService positionService, PositionRepository positionRepository) {
        this.positionService = positionService;
        this.positionRepository = positionRepository;
    }

    /**
     * {@code POST  /positions} : Create a new position.
     *
     * @param position the position to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new position, or with status {@code 400 (Bad Request)} if the position has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Position> createPosition(@Valid @RequestBody Position position) throws URISyntaxException {
        LOG.debug("REST request to save Position : {}", position);
        if (position.getId() != null) {
            throw new BadRequestAlertException("A new position cannot already have an ID", ENTITY_NAME, "idexists");
        }
        position = positionService.save(position);
        return ResponseEntity.created(new URI("/api/positions/" + position.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, position.getId().toString()))
            .body(position);
    }

    /**
     * {@code PUT  /positions/:id} : Updates an existing position.
     *
     * @param id the id of the position to save.
     * @param position the position to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated position,
     * or with status {@code 400 (Bad Request)} if the position is not valid,
     * or with status {@code 500 (Internal Server Error)} if the position couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Position> updatePosition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Position position
    ) throws URISyntaxException {
        LOG.debug("REST request to update Position : {}, {}", id, position);
        if (position.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, position.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        position = positionService.update(position);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, position.getId().toString()))
            .body(position);
    }

    /**
     * {@code PATCH  /positions/:id} : Partial updates given fields of an existing position, field will ignore if it is null
     *
     * @param id the id of the position to save.
     * @param position the position to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated position,
     * or with status {@code 400 (Bad Request)} if the position is not valid,
     * or with status {@code 404 (Not Found)} if the position is not found,
     * or with status {@code 500 (Internal Server Error)} if the position couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Position> partialUpdatePosition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Position position
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Position partially : {}, {}", id, position);
        if (position.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, position.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Position> result = positionService.partialUpdate(position);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, position.getId().toString())
        );
    }

    /**
     * {@code GET  /positions} : get all the positions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of positions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Position>> getAllPositions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Positions");
        Page<Position> page = positionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /positions/:id} : get the "id" position.
     *
     * @param id the id of the position to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the position, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Position> getPosition(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Position : {}", id);
        Optional<Position> position = positionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(position);
    }

    /**
     * {@code DELETE  /positions/:id} : delete the "id" position.
     *
     * @param id the id of the position to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Position : {}", id);
        positionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
