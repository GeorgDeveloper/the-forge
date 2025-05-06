package ru.georgdeveloper.myapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.Profession;

/**
 * Репозиторий Spring Data JPA для профессиональной организации.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long> {
    @EntityGraph(attributePaths = { "employees" })
    Optional<Profession> findWithEmployeesById(Long id);
}
