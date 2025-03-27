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
import ru.georgdeveloper.myapp.domain.Team;
import ru.georgdeveloper.myapp.repository.TeamRepository;
import ru.georgdeveloper.myapp.service.TeamService;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST контроллер для управления рабочими командами.
 * Предоставляет CRUD-операции для сущности {@link ru.georgdeveloper.myapp.domain.Team}.
 */
@RestController
@RequestMapping("/api/teams")
public class TeamResource {

    private static final Logger LOG = LoggerFactory.getLogger(TeamResource.class);

    // Название сущности для сообщений об ошибках
    private static final String ENTITY_NAME = "team";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeamService teamService;
    private final TeamRepository teamRepository;

    /**
     * Конструктор контроллера.
     *
     * @param teamService сервис для работы с командами
     * @param teamRepository репозиторий команд
     */
    public TeamResource(TeamService teamService, TeamRepository teamRepository) {
        this.teamService = teamService;
        this.teamRepository = teamRepository;
    }

    /**
     * Создает новую рабочую команду.
     * POST /api/teams
     *
     * @param team данные новой команды
     * @return ResponseEntity с созданной командой или ошибкой 400 если ID уже существует
     * @throws URISyntaxException при некорректном URI
     */
    @PostMapping("")
    public ResponseEntity<Team> createTeam(@Valid @RequestBody Team team) throws URISyntaxException {
        LOG.debug("Запрос на создание команды: {}", team);
        if (team.getId() != null) {
            throw new BadRequestAlertException("Новая команда не может иметь ID", ENTITY_NAME, "idexists");
        }
        team = teamService.save(team);
        return ResponseEntity.created(new URI("/api/teams/" + team.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, team.getId().toString()))
            .body(team);
    }

    /**
     * Полностью обновляет данные команды.
     * PUT /api/teams/{id}
     *
     * @param id ID команды
     * @param team обновленные данные команды
     * @return ResponseEntity с обновленной командой или кодом ошибки
     * @throws URISyntaxException при некорректном URI
     */
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Team team)
        throws URISyntaxException {
        LOG.debug("Запрос на обновление команды: ID {}, Данные: {}", id, team);
        if (team.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, team.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!teamRepository.existsById(id)) {
            throw new BadRequestAlertException("Команда не найдена", ENTITY_NAME, "idnotfound");
        }

        team = teamService.update(team);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, team.getId().toString()))
            .body(team);
    }

    /**
     * Частично обновляет данные команды.
     * PATCH /api/teams/{id}
     *
     * @param id ID команды
     * @param team данные для частичного обновления
     * @return ResponseEntity с обновленной командой или кодом ошибки
     * @throws URISyntaxException при некорректном URI
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Team> partialUpdateTeam(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Team team
    ) throws URISyntaxException {
        LOG.debug("Запрос на частичное обновление команды: ID {}, Данные: {}", id, team);
        if (team.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, team.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!teamRepository.existsById(id)) {
            throw new BadRequestAlertException("Команда не найдена", ENTITY_NAME, "idnotfound");
        }

        Optional<Team> result = teamService.partialUpdate(team);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, team.getId().toString())
        );
    }

    /**
     * Получает список команд с пагинацией.
     * GET /api/teams
     *
     * @param pageable параметры пагинации
     * @return ResponseEntity со списком команд и заголовками пагинации
     */
    @GetMapping("")
    public ResponseEntity<List<Team>> getAllTeams(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("Запрос на получение списка команд");
        Page<Team> page = teamService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Получает команду по ID.
     * GET /api/teams/{id}
     *
     * @param id ID команды
     * @return ResponseEntity с командой или 404 если не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable("id") Long id) {
        LOG.debug("Запрос на получение команды: ID {}", id);
        Optional<Team> team = teamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(team);
    }

    /**
     * Удаляет команду.
     * DELETE /api/teams/{id}
     *
     * @param id ID команды для удаления
     * @return ResponseEntity с кодом 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable("id") Long id) {
        LOG.debug("Запрос на удаление команды: ID {}", id);
        teamService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
