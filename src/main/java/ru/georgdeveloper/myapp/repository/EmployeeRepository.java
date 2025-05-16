package ru.georgdeveloper.myapp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
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
     *
     * @param id идентификатор сотрудника
     * @return Optional с сотрудником и загруженными связями
     */
    default Optional<Employee> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    /**
     * Находит всех сотрудников с eagerly загруженными связанными сущностями.
     *
     * @return список сотрудников с загруженными связями
     */
    default List<Employee> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    /**
     * Находит всех сотрудников из команд текущего пользователя (с пагинацией).
     *
     * @param pageable параметры пагинации
     */
    @Query("SELECT e FROM Employee e WHERE e.team IN (SELECT uta.team FROM UserTeamAccess uta WHERE uta.user.login = :currentUserLogin)")
    Page<Employee> findAllByUserTeams(@Param("currentUserLogin") String currentUserLogin, Pageable pageable);

    /**
     * Находит всех сотрудников с eagerly загруженными связанными сущностями (с пагинацией).
     *
     * @param employeeIds параметры пагинации
     */
    @EntityGraph(attributePaths = { "team", "position" }) // Загружаем только то, что нужно
    @Query("SELECT e FROM Employee e WHERE e.id IN :employeeIds")
    List<Employee> findAllWithTeamAndPosition(@Param("employeeIds") List<Long> employeeIds);

    /**
     * Находит всех сотрудников из команд текущего пользователя
     */
    @Query("SELECT e FROM Employee e JOIN e.team t JOIN t.userAccesses ua WHERE ua.user.login = :currentUserLogin")
    List<Employee> findAllForCurrentUser(@Param("currentUserLogin") String currentUserLogin);

    /**
     * Находит всех сотрудников из команд текущего пользователя (с пагинацией)
     */
    @Query("SELECT e FROM Employee e JOIN e.team t JOIN t.userAccesses ua WHERE ua.user.login = :currentUserLogin")
    Page<Employee> findAllForCurrentUser(@Param("currentUserLogin") String currentUserLogin, Pageable pageable);

    /**
     * Находит одного сотрудника с проверкой доступа текущего пользователя
     */
    @Query("SELECT e FROM Employee e JOIN e.team t JOIN t.userAccesses ua WHERE e.id = :employeeId AND ua.user.login = :currentUserLogin")
    Optional<Employee> findOneForCurrentUser(@Param("employeeId") Long employeeId, @Param("currentUserLogin") String currentUserLogin);
}
