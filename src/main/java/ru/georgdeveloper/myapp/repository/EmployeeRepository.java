package ru.georgdeveloper.myapp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.Employee;

/**
 * Spring Data JPA репозиторий для сущности Employee.
 * При расширении этого класса следует также расширять EmployeeRepositoryWithBagRelationships.
 * Для дополнительной информации см.: https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface EmployeeRepository extends EmployeeRepositoryWithBagRelationships, JpaRepository<Employee, Long> {
    /**
     * Находит одного сотрудника с eagerly загруженными связанными сущностями.
     * @param id идентификатор сотрудника
     * @return Optional с сотрудником и загруженными связями
     */
    default Optional<Employee> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    /**
     * Находит всех сотрудников с eagerly загруженными связанными сущностями.
     * @return список сотрудников с загруженными связями
     */
    default List<Employee> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    /**
     * Находит всех сотрудников с eagerly загруженными связанными сущностями (с пагинацией).
     * @param pageable параметры пагинации
     * @return страница сотрудников с загруженными связями
     */
    default Page<Employee> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
