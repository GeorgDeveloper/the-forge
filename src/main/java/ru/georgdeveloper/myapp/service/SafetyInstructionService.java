package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.SafetyInstruction;

/**
 * Service Interface for managing {@link ru.georgdeveloper.myapp.domain.SafetyInstruction}.
 */
public interface SafetyInstructionService {
    /**
     * Save a safetyInstruction.
     *
     * @param safetyInstruction the entity to save.
     * @return the persisted entity.
     */
    SafetyInstruction save(SafetyInstruction safetyInstruction);

    /**
     * Updates a safetyInstruction.
     *
     * @param safetyInstruction the entity to update.
     * @return the persisted entity.
     */
    SafetyInstruction update(SafetyInstruction safetyInstruction);

    /**
     * Partially updates a safetyInstruction.
     *
     * @param safetyInstruction the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SafetyInstruction> partialUpdate(SafetyInstruction safetyInstruction);

    /**
     * Get all the safetyInstructions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SafetyInstruction> findAll(Pageable pageable);

    /**
     * Get the "id" safetyInstruction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SafetyInstruction> findOne(Long id);

    /**
     * Delete the "id" safetyInstruction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
