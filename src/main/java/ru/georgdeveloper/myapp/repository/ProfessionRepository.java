package ru.georgdeveloper.myapp.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.Profession;

/**
 * Репозиторий Spring Data JPA для профессиональной организации.
 */
@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long> {
    @Query("SELECT p FROM Profession p LEFT JOIN FETCH p.employees WHERE p.id = :id")
    Optional<Profession> findWithEmployeesById(@Param("id") Long id);

    // Убираем JOIN FETCH для пагинированного запроса
    @Query("SELECT p FROM Profession p")
    Page<Profession> findAllWithEmployees(Pageable pageable);
}
