package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.SafetyInstruction;

/**
 * Spring Data JPA repository for the SafetyInstruction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SafetyInstructionRepository extends JpaRepository<SafetyInstruction, Long> {}
