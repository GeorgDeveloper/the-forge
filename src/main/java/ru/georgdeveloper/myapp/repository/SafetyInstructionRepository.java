package ru.georgdeveloper.myapp.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.SafetyInstruction;

/**
 * Репозиторий Spring Data JPA для объекта SafetyInstruction.
 */
@Repository
public interface SafetyInstructionRepository extends JpaRepository<SafetyInstruction, Long> {
    /**
     * Находит SafetyInstruction с загруженными связанными объектами profession и position
     * @param id идентификатор SafetyInstruction
     * @return Optional с найденной сущностью
     */
    @Query("SELECT si FROM SafetyInstruction si LEFT JOIN FETCH si.profession LEFT JOIN FETCH si.position WHERE si.id = :id")
    Optional<SafetyInstruction> findWithProfessionAndPositionById(Long id);

    /**
     * Получает все SafetyInstruction с загруженными связанными объектами profession и position
     * @param pageable параметры пагинации
     * @return страница с SafetyInstruction
     */
    @Query("SELECT si FROM SafetyInstruction si LEFT JOIN FETCH si.profession LEFT JOIN FETCH si.position")
    Page<SafetyInstruction> findAllWithProfessionAndPosition(Pageable pageable);
}
