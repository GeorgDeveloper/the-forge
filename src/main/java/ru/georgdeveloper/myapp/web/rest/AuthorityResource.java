package ru.georgdeveloper.myapp.web.rest;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.georgdeveloper.myapp.domain.Authority;
import ru.georgdeveloper.myapp.repository.AuthorityRepository;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST контроллер для управления {@link ru.georgdeveloper.myapp.domain.Authority}.
 */
@RestController
@RequestMapping("/api/authorities")
@Transactional
public class AuthorityResource {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorityResource.class);

    private static final String ENTITY_NAME = "adminAuthority";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthorityRepository authorityRepository;

    /**
     * Конструктор контроллера.
     * @param authorityRepository репозиторий для работы с ролями
     */
    public AuthorityResource(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    /**
     * Создает новую роль.
     * POST /api/authorities
     *
     * @param authority данные новой роли
     * @return ResponseEntity с созданной ролью или кодом ошибки 400
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Authority> createAuthority(@Valid @RequestBody Authority authority) throws URISyntaxException {
        LOG.debug("REST запрос на создание роли: {}", authority);
        if (authorityRepository.existsById(authority.getName())) {
            throw new BadRequestAlertException("Роль уже существует", ENTITY_NAME, "idexists");
        }
        authority = authorityRepository.save(authority);
        return ResponseEntity.created(new URI("/api/authorities/" + authority.getName()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, authority.getName()))
            .body(authority);
    }

    /**
     * Получает список всех ролей.
     * GET /api/authorities
     *
     * @return список всех ролей
     */
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<Authority> getAllAuthorities() {
        LOG.debug("REST запрос на получение всех ролей");
        return authorityRepository.findAll();
    }

    /**
     * Получает роль по идентификатору.
     * GET /api/authorities/{id}
     *
     * @param id идентификатор роли
     * @return ResponseEntity с найденной ролью или кодом 404
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Authority> getAuthority(@PathVariable("id") String id) {
        LOG.debug("REST запрос на получение роли: {}", id);
        Optional<Authority> authority = authorityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(authority);
    }

    /**
     * Удаляет роль по идентификатору.
     * DELETE /api/authorities/{id}
     *
     * @param id идентификатор удаляемой роли
     * @return ResponseEntity с кодом 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteAuthority(@PathVariable("id") String id) {
        LOG.debug("REST запрос на удаление роли: {}", id);
        authorityRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
