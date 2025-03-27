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
import ru.georgdeveloper.myapp.domain.Employee;
import ru.georgdeveloper.myapp.repository.EmployeeRepository;
import ru.georgdeveloper.myapp.service.EmployeeService;
import ru.georgdeveloper.myapp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST контроллер для управления сущностью {@link ru.georgdeveloper.myapp.domain.Employee}.
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeResource.class);

    private static final String ENTITY_NAME = "employee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    /**
     * Конструктор контроллера.
     *
     * @param employeeService сервис для работы с сотрудниками
     * @param employeeRepository репозиторий сотрудников
     */
    public EmployeeResource(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Создает нового сотрудника.
     * POST /api/employees
     *
     * @param employee данные нового сотрудника
     * @return ResponseEntity с созданным сотрудником или ошибкой 400 если ID уже существует
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PostMapping("")
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) throws URISyntaxException {
        LOG.debug("REST запрос на создание сотрудника: {}", employee);
        if (employee.getId() != null) {
            throw new BadRequestAlertException("Новый сотрудник не может иметь ID", ENTITY_NAME, "idexists");
        }
        employee = employeeService.save(employee);
        return ResponseEntity.created(new URI("/api/employees/" + employee.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employee.getId().toString()))
            .body(employee);
    }

    /**
     * Полностью обновляет данные сотрудника.
     * PUT /api/employees/{id}
     *
     * @param id ID сотрудника для обновления
     * @param employee новые данные сотрудника
     * @return ResponseEntity с обновленным сотрудником или кодом ошибки
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Employee employee
    ) throws URISyntaxException {
        LOG.debug("REST запрос на обновление сотрудника: {}, {}", id, employee);
        if (employee.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employee.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Сотрудник не найден", ENTITY_NAME, "idnotfound");
        }

        employee = employeeService.update(employee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employee.getId().toString()))
            .body(employee);
    }

    /**
     * Частично обновляет данные сотрудника.
     * PATCH /api/employees/{id}
     *
     * @param id ID сотрудника для обновления
     * @param employee данные для частичного обновления
     * @return ResponseEntity с обновленным сотрудником или кодом ошибки
     * @throws URISyntaxException при некорректном синтаксисе URI
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Employee> partialUpdateEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Employee employee
    ) throws URISyntaxException {
        LOG.debug("REST запрос на частичное обновление сотрудника: {}, {}", id, employee);
        if (employee.getId() == null) {
            throw new BadRequestAlertException("Неверный ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employee.getId())) {
            throw new BadRequestAlertException("Несоответствие ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Сотрудник не найден", ENTITY_NAME, "idnotfound");
        }

        Optional<Employee> result = employeeService.partialUpdate(employee);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employee.getId().toString())
        );
    }

    /**
     * Получает список сотрудников с пагинацией.
     * GET /api/employees
     *
     * @param pageable параметры пагинации
     * @param eagerload флаг загрузки связанных сущностей (для many-to-many)
     * @return ResponseEntity со списком сотрудников
     */
    @GetMapping("")
    public ResponseEntity<List<Employee>> getAllEmployees(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST запрос на получение страницы сотрудников");
        Page<Employee> page;
        if (eagerload) {
            page = employeeService.findAllWithEagerRelationships(pageable);
        } else {
            page = employeeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Получает сотрудника по ID.
     * GET /api/employees/{id}
     *
     * @param id ID сотрудника
     * @return ResponseEntity с найденным сотрудником или 404 если не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") Long id) {
        LOG.debug("REST запрос на получение сотрудника: {}", id);
        Optional<Employee> employee = employeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employee);
    }

    /**
     * Удаляет сотрудника по ID.
     * DELETE /api/employees/{id}
     *
     * @param id ID сотрудника для удаления
     * @return ResponseEntity с кодом 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        LOG.debug("REST запрос на удаление сотрудника: {}", id);
        employeeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
