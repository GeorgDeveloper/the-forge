package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Employee;
import ru.georgdeveloper.myapp.repository.EmployeeRepository;
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

    /**
     * Конструктор с внедрением зависимости репозитория.
     * @param employeeRepository репозиторий для работы с Employee
     */
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Сохраняет нового сотрудника.
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
     * @param pageable параметры пагинации
     * @return страница с сотрудниками и загруженными связями
     */
    public Page<Employee> findAllWithEagerRelationships(Pageable pageable) {
        return employeeRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Находит сотрудника по ID с eager-загрузкой связанных сущностей.
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
     * @param id идентификатор сотрудника для удаления
     */
    @Override
    public void delete(Long id) {
        LOG.debug("Запрос на удаление сотрудника с ID: {}", id);
        employeeRepository.deleteById(id);
    }
}
