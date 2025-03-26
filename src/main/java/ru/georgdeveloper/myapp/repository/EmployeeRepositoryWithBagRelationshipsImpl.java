package ru.georgdeveloper.myapp.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.georgdeveloper.myapp.domain.Employee;

/**
 * Реализация утилитарного репозитория для загрузки bag-отношений (коллекций) сущности Employee.
 * Решает проблему MultipleBagFetchException, следуя подходу Vlad Mihalcea:
 * https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EmployeeRepositoryWithBagRelationshipsImpl implements EmployeeRepositoryWithBagRelationships {

    // Параметр для запроса - ID сотрудника
    private static final String ID_PARAMETER = "id";
    // Параметр для запроса - список сотрудников
    private static final String EMPLOYEES_PARAMETER = "employees";

    // Инъекция EntityManager для работы с JPA
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Загружает связанные сущности для одного Employee в Optional.
     * @param employee Optional с Employee
     * @return Optional с Employee с загруженными связями
     */
    @Override
    public Optional<Employee> fetchBagRelationships(Optional<Employee> employee) {
        return employee.map(this::fetchProfessions);
    }

    /**
     * Загружает связанные сущности для страницы Employee.
     * @param employees страница Employee
     * @return новая страница с Employee с загруженными связями
     */
    @Override
    public Page<Employee> fetchBagRelationships(Page<Employee> employees) {
        return new PageImpl<>(
            fetchBagRelationships(employees.getContent()), // Загружаем связи для контента
            employees.getPageable(),
            employees.getTotalElements()
        );
    }

    /**
     * Загружает связанные сущности для списка Employee.
     * @param employees список Employee
     * @return список Employee с загруженными связями
     */
    @Override
    public List<Employee> fetchBagRelationships(List<Employee> employees) {
        return Optional.of(employees).map(this::fetchProfessions).orElse(Collections.emptyList());
    }

    /**
     * Загружает профессии (bag-отношение) для одного сотрудника.
     * @param result сотрудник, для которого загружаем профессии
     * @return сотрудник с загруженными профессиями
     */
    Employee fetchProfessions(Employee result) {
        return entityManager
            .createQuery(
                "select employee from Employee employee left join fetch employee.professions where employee.id = :id",
                Employee.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    /**
     * Загружает профессии (bag-отношение) для списка сотрудников с сохранением исходного порядка.
     * @param employees список сотрудников
     * @return список сотрудников с загруженными профессиями в исходном порядке
     */
    List<Employee> fetchProfessions(List<Employee> employees) {
        // Сохраняем исходный порядок сотрудников в HashMap
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, employees.size()).forEach(index -> order.put(employees.get(index).getId(), index));

        // Загружаем сотрудников с профессиями
        List<Employee> result = entityManager
            .createQuery(
                "select employee from Employee employee left join fetch employee.professions where employee in :employees",
                Employee.class
            )
            .setParameter(EMPLOYEES_PARAMETER, employees)
            .getResultList();

        // Восстанавливаем исходный порядок
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));

        return result;
    }
}
