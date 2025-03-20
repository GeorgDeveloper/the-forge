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
import ru.georgdeveloper.myapp.domain.Profession;
import ru.georgdeveloper.myapp.repository.ProfessionRepository;
import ru.georgdeveloper.myapp.service.ProfessionService;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.georgdeveloper.myapp.domain.Profession}.
 */
@RestController
@RequestMapping("/api/professions")
public class ProfessionResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionResource.class);

    private static final String ENTITY_NAME = "profession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionService professionService;

    private final ProfessionRepository professionRepository;

    public ProfessionResource(ProfessionService professionService, ProfessionRepository professionRepository) {
        this.professionService = professionService;
        this.professionRepository = professionRepository;
    }

    /**
     * {@code POST  /professions} : Create a new profession.
     *
     * @param profession the profession to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profession, or with status {@code 400 (Bad Request)} if the profession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Profession> createProfession(@Valid @RequestBody Profession profession) throws URISyntaxException {
        LOG.debug("REST request to save Profession : {}", profession);
        if (profession.getId() != null) {
            throw new BadRequestAlertException("A new profession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        profession = professionService.save(profession);
        return ResponseEntity.created(new URI("/api/professions/" + profession.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, profession.getId().toString()))
            .body(profession);
    }

    /**
     * {@code PUT  /professions/:id} : Updates an existing profession.
     *
     * @param id the id of the profession to save.
     * @param profession the profession to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profession,
     * or with status {@code 400 (Bad Request)} if the profession is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profession couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Profession> updateProfession(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Profession profession
    ) throws URISyntaxException {
        LOG.debug("REST request to update Profession : {}, {}", id, profession);
        if (profession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profession.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        profession = professionService.update(profession);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profession.getId().toString()))
            .body(profession);
    }

    /**
     * {@code PATCH  /professions/:id} : Partial updates given fields of an existing profession, field will ignore if it is null
     *
     * @param id the id of the profession to save.
     * @param profession the profession to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profession,
     * or with status {@code 400 (Bad Request)} if the profession is not valid,
     * or with status {@code 404 (Not Found)} if the profession is not found,
     * or with status {@code 500 (Internal Server Error)} if the profession couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Profession> partialUpdateProfession(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Profession profession
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Profession partially : {}, {}", id, profession);
        if (profession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profession.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Profession> result = professionService.partialUpdate(profession);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profession.getId().toString())
        );
    }

    /**
     * {@code GET  /professions} : get all the professions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Profession>> getAllProfessions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Professions");
        Page<Profession> page = professionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professions/:id} : get the "id" profession.
     *
     * @param id the id of the profession to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profession, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Profession> getProfession(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Profession : {}", id);
        Optional<Profession> profession = professionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profession);
    }

    /**
     * {@code DELETE  /professions/:id} : delete the "id" profession.
     *
     * @param id the id of the profession to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfession(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Profession : {}", id);
        professionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
