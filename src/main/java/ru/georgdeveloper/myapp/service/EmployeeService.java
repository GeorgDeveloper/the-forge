package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.Employee;

/**
 * Интерфейс сервиса для управления {@link ru.georgdeveloper.myapp.domain.Employee}.
 */
public interface EmployeeService {
    /**
     * Сохранить сотрудника.
     *
     * @param employee сущность для сохранения.
     * @return сохраненная сущность.
     */
    Employee save(Employee employee);

    /**
     * Обновить сотрудника.
     *
     * @param employee сущность для обновления.
     * @return обновленная сущность.
     */
    Employee update(Employee employee);

    /**
     * Частично обновить сотрудника.
     *
     * @param employee сущность для частичного обновления.
     * @return обновленная сущность.
     */
    Optional<Employee> partialUpdate(Employee employee);

    /**
     * Получить всех сотрудников.
     *
     * @param pageable информация о пагинации.
     * @return список сущностей.
     */
    Page<Employee> findAll(Pageable pageable);

    /**
     * Получить всех сотрудников с жадной загрузкой many-to-many отношений.
     *
     * @param pageable информация о пагинации.
     * @return список сущностей.
     */
    Page<Employee> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Получить сотрудника по "id".
     *
     * @param id идентификатор сущности.
     * @return сущность.
     */
    Optional<Employee> findOne(Long id);

    /**
     * Удалить сотрудника по "id".
     *
     * @param id идентификатор сущности.
     */
    void delete(Long id);
}
