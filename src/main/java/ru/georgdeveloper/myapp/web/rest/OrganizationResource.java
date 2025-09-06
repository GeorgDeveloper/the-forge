package ru.georgdeveloper.myapp.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.georgdeveloper.myapp.domain.Organization;
import ru.georgdeveloper.myapp.repository.OrganizationRepository;
import ru.georgdeveloper.myapp.service.OrganizationService;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationResource.class);
    private static final String ENTITY_NAME = "organization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationService organizationService;
    private final OrganizationRepository organizationRepository;

    public OrganizationResource(OrganizationService organizationService, OrganizationRepository organizationRepository) {
        this.organizationService = organizationService;
        this.organizationRepository = organizationRepository;
    }

    @PostMapping("")
    public ResponseEntity<Organization> create(@Valid @RequestBody Organization organization) throws URISyntaxException {
        LOG.debug("Создание организации: {}", organization);
        if (organization.getId() != null) {
            throw new BadRequestAlertException("Новая организация не может иметь ID", ENTITY_NAME, "idexists");
        }
        organization = organizationService.save(organization);
        return ResponseEntity.created(new URI("/api/organizations/" + organization.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, organization.getId().toString()))
            .body(organization);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organization> update(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Organization organization
    ) throws URISyntaxException {
        LOG.debug("Полное обновление организации: {}, {}", id, organization);
        if (organization.getId() == null) throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        if (!id.equals(organization.getId())) throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        if (!organizationRepository.existsById(id)) throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        Organization result = organizationService.update(organization);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organization.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Organization> partialUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Organization organization
    ) throws URISyntaxException {
        LOG.debug("Частичное обновление организации: {}, {}", id, organization);
        if (organization.getId() == null) throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        if (!id.equals(organization.getId())) throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        if (!organizationRepository.existsById(id)) throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        return organizationService
            .partialUpdate(organization)
            .map(result ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result)
            )
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("")
    public ResponseEntity<java.util.List<Organization>> getAll(Pageable pageable) {
        LOG.debug("Запрос на получение списка организаций");
        Page<Organization> page = organizationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOne(@PathVariable("id") Long id) {
        return organizationRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        organizationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
