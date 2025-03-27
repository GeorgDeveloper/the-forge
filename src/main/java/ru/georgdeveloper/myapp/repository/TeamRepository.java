package ru.georgdeveloper.myapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.Team;

/**
 * Репозиторий Spring Data JPA для объекта Team.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    // Добавьте этот метод
    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.employees WHERE t.id = :id")
    Optional<Team> findByIdWithEmployees(@Param("id") Long id);
    //    @EntityGraph(attributePaths = {"employees"})  // ← Явно указываем загрузку сотрудников
    //    Optional<Team> findWithEmployeesById(Long id);
}
