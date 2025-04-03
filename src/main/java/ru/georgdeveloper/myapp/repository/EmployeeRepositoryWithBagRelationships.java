package ru.georgdeveloper.myapp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.georgdeveloper.myapp.domain.Employee;

/**
 * Интерфейс репозитория для работы с агрегациями (bag relationships) сущности Employee.
 * Предоставляет методы для загрузки связанных сущностей (ленивых коллекций) для Employee.
 */
public interface EmployeeRepositoryWithBagRelationships {
    /**
     * Загружает связанные сущности (bag relationships) для одного Employee, обернутого в Optional.
     *
     * @param employee Optional с Employee, для которого нужно загрузить связи
     * @return Optional с тем же Employee, но с загруженными связанными сущностями
     */
    Optional<Employee> fetchBagRelationships(Optional<Employee> employee);

    /**
     * Загружает связанные сущности (bag relationships) для списка Employee.
     *
     * @param employees список Employee, для которых нужно загрузить связи
     * @return тот же список Employee, но с загруженными связанными сущностями
     */
    List<Employee> fetchBagRelationships(List<Employee> employees);
    /**
     * Загружает связанные сущности (bag relationships) для страницы (Page) Employee.
     *
     * @param pageable страница Employee, для которых нужно загрузить связи
     * @return та же страница Employee, но с загруженными связанными сущностями
     */
    //    Page<Employee> fetchBagRelationships(Page<Employee> employees);

}
