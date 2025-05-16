package ru.georgdeveloper.myapp.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Employee;
import ru.georgdeveloper.myapp.domain.Profession;
import ru.georgdeveloper.myapp.repository.EmployeeRepository;
import ru.georgdeveloper.myapp.repository.ProfessionRepository;
import ru.georgdeveloper.myapp.service.EmployeeService;

/**
 * Реализация сервиса для управления сущностью {@link Employee}.
 * Обеспечивает CRUD операции и бизнес-логику работы с сотрудниками.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final ProfessionRepository professionRepository;

    /**
     * Конструктор с внедрением зависимости репозитория.
     *
     * @param employeeRepository репозиторий для работы с Employee
     */
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ProfessionRepository professionRepository) {
        this.employeeRepository = employeeRepository;
        this.professionRepository = professionRepository;
    }

    /**
     * Сохраняет нового сотрудника.
     *
     * @param employee сущность для сохранения
     * @return сохраненная сущность сотрудника
     */
    @Override
    public Employee save(Employee employee) {
        LOG.debug("Запрос на сохранение сотрудника: {}", employee);
        return employeeRepository.save(employee);
    }

    /**
     * Обновляет существующего сотрудника.
     *
     * @param employee сущность с обновленными данными
     * @return обновленная сущность сотрудника
     */
    @Override
    public Employee update(Employee employee) {
        LOG.debug("Запрос на обновление сотрудника: {}", employee);
        return employeeRepository.save(employee);
    }

    /**
     * Частично обновляет данные сотрудника.
     *
     * @param employee сущность с обновляемыми полями
     * @return Optional с обновленным сотрудником, если он существует
     */
    @Override
    public Optional<Employee> partialUpdate(Employee employee) {
        LOG.debug("Запрос на частичное обновление сотрудника: {}", employee);

        return employeeRepository
            .findById(employee.getId())
            .map(existingEmployee -> {
                // Обновляем только те поля, которые не null
                if (employee.getFirstName() != null) {
                    existingEmployee.setFirstName(employee.getFirstName());
                }
                if (employee.getLastName() != null) {
                    existingEmployee.setLastName(employee.getLastName());
                }
                if (employee.getBirthDate() != null) {
                    existingEmployee.setBirthDate(employee.getBirthDate());
                }
                if (employee.getEmployeeNumber() != null) {
                    existingEmployee.setEmployeeNumber(employee.getEmployeeNumber());
                }
                if (employee.getHireDate() != null) {
                    existingEmployee.setHireDate(employee.getHireDate());
                }

                return existingEmployee;
            })
            .map(employeeRepository::save);
    }

    /**
     * Получает всех сотрудников с пагинацией (без eager-загрузки связей).
     *
     * @param pageable параметры пагинации
     * @return страница с сотрудниками
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Employee> findAll(Pageable pageable) {
        LOG.debug("Запрос на получение всех сотрудников");
        return employeeRepository.findAll(pageable);
    }

    /**
     * Получает всех сотрудников с eager-загрузкой связанных сущностей.
     *
     * @param pageable параметры пагинации
     * @return страница с сотрудниками и загруженными связями
     */
    @Transactional(readOnly = true)
    @Override
    public Page<Employee> findAllWithEagerRelationships(String currentUserLogin, Pageable pageable) {
        // Сначала получаем страницу ID с пагинацией
        Page<Employee> employeePage = employeeRepository.findAllByUserTeams(currentUserLogin, pageable);

        // Затем загружаем связанные сущности для этих ID
        List<Long> employeeIds = employeePage.getContent().stream().map(Employee::getId).collect(Collectors.toList());

        List<Employee> employeesWithRelationships = employeeRepository.findAllWithTeamAndPosition(employeeIds);

        // Собираем результат
        return new PageImpl<>(employeesWithRelationships, pageable, employeePage.getTotalElements());
    }

    /**
     * Находит сотрудника по ID с eager-загрузкой связанных сущностей.
     *
     * @param id идентификатор сотрудника
     * @return Optional с найденным сотрудником
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findOne(Long id) {
        LOG.debug("Запрос на получение сотрудника с ID: {}", id);
        return employeeRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Удаляет сотрудника по ID.
     *
     * @param id идентификатор сотрудника для удаления
     */
    @Override
    public void delete(Long id) {
        LOG.debug("Запрос на удаление сотрудника с ID: {}", id);
        employeeRepository.deleteById(id);
    }

    /**
     * Находит всех сотрудников из команд текущего пользователя
     */
    @Override
    public List<Employee> findAllForCurrentUser(String currentUserLogin) {
        return employeeRepository.findAllForCurrentUser(currentUserLogin);
    }

    /**
     * Находит всех сотрудников из команд текущего пользователя (с пагинацией)
     */
    @Override
    public Page<Employee> findAllForCurrentUser(String currentUserLogin, Pageable pageable) {
        return employeeRepository.findAllForCurrentUser(currentUserLogin, pageable);
    }

    /**
     * Находит одного сотрудника с проверкой доступа текущего пользователя
     */
    @Override
    public Optional<Employee> findOneForCurrentUser(Long employeeId, String currentUserLogin) {
        return employeeRepository.findOneForCurrentUser(employeeId, currentUserLogin);
    }

    @Override
    public Optional<Employee> findOneWithProfessions(Long id) {
        return employeeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void deleteProfessionFromEmployee(Employee employee, Long professionsId) {
        // Получасе профессию
        Profession savedProfession = professionRepository.getReferenceById(professionsId);

        // Затем обновляем связи с сотрудниками
        if (savedProfession.getEmployees() != null) {
            Employee managedEmployee = employeeRepository.findById(employee.getId()).orElseThrow();

            // Инициализируем коллекции, если нужно
            if (managedEmployee.getProfessions() == null) {
                managedEmployee.setProfessions(new HashSet<>());
            }

            // Добавляем профессию сотруднику
            managedEmployee.getProfessions().remove(savedProfession);
            employeeRepository.save(managedEmployee);

            savedProfession.getEmployees().remove(employee);

            professionRepository.save(savedProfession);
        }
    }
}
