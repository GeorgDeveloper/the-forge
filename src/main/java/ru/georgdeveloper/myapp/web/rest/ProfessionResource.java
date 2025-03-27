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
 * REST контроллер для управления профессиями {@link ru.georgdeveloper.myapp.domain.Profession}.
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

    /**
     * Конструктор контроллера.
     *
     * @param professionService сервис для работы с профессиями
     * @param professionRepository репозиторий профессий
     */
    public ProfessionResource(ProfessionService professionService, ProfessionRepository professionRepository) {
        this.professionService = professionService;
        this.professionRepository = professionRepository;
    }

    /**
     * Создает новую профессию.
     * POST /api/professions
     *
     * @param profession данные новой профессии
     * @return ResponseEntity с созданной профессией или ошибкой 400 если ID уже существует
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PostMapping("")
    public ResponseEntity<Profession> createProfession(@Valid @RequestBody Profession profession) throws URISyntaxException {
        LOG.debug("REST запрос на создание профессии: {}", profession);
        if (profession.getId() != null) {
            throw new BadRequestAlertException("Новая профессия не может иметь ID", ENTITY_NAME, "idexists");
        }
        profession = professionService.save(profession);
        return ResponseEntity.created(new URI("/api/professions/" + profession.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, profession.getId().toString()))
            .body(profession);
    }

    /**
     * Полностью обновляет данные профессии.
     * PUT /api/professions/{id}
     *
     * @param id ID профессии для обновления
     * @param profession новые данные профессии
     * @return ResponseEntity с обновленной профессией или кодом ошибки
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PutMapping("/{id}")
    public ResponseEntity<Profession> updateProfession(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Profession profession
    ) throws URISyntaxException {
        LOG.debug("REST запрос на обновление профессии: {}, {}", id, profession);
        if (profession.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profession.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionRepository.existsById(id)) {
            throw new BadRequestAlertException("Профессия не найдена", ENTITY_NAME, "idnotfound");
        }

        profession = professionService.update(profession);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profession.getId().toString()))
            .body(profession);
    }

    /**
     * Частично обновляет данные профессии.
     * PATCH /api/professions/{id}
     *
     * @param id ID профессии для обновления
     * @param profession данные для частичного обновления
     * @return ResponseEntity с обновленной профессией или кодом ошибки
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Profession> partialUpdateProfession(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Profession profession
    ) throws URISyntaxException {
        LOG.debug("REST запрос на частичное обновление профессии: {}, {}", id, profession);
        if (profession.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profession.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionRepository.existsById(id)) {
            throw new BadRequestAlertException("Профессия не найдена", ENTITY_NAME, "idnotfound");
        }

        Optional<Profession> result = professionService.partialUpdate(profession);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profession.getId().toString())
        );
    }

    /**
     * Получает список профессий с пагинацией.
     * GET /api/professions
     *
     * @param pageable параметры пагинации
     * @return ResponseEntity со списком профессий
     */
    @GetMapping("")
    public ResponseEntity<List<Profession>> getAllProfessions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST запрос на получение страницы профессий");
        Page<Profession> page = professionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Получает профессию по ID.
     * GET /api/professions/{id}
     *
     * @param id ID профессии
     * @return ResponseEntity с найденной профессией или 404 если не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<Profession> getProfession(@PathVariable("id") Long id) {
        LOG.debug("REST запрос на получение профессии: {}", id);
        Optional<Profession> profession = professionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profession);
    }

    /**
     * Удаляет профессию по ID.
     * DELETE /api/professions/{id}
     *
     * @param id ID профессии для удаления
     * @return ResponseEntity с кодом 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfession(@PathVariable("id") Long id) {
        LOG.debug("REST запрос на удаление профессии: {}", id);
        professionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
