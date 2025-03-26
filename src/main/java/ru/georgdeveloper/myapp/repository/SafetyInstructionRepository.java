package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.SafetyInstruction;

/**
 * Репозиторий Spring Data JPA для объекта SafetyInstruction.
 */
@SuppressWarnings("unused")
@Repository
public interface SafetyInstructionRepository extends JpaRepository<SafetyInstruction, Long> {}
