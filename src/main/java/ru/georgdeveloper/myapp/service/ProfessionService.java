package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.Profession;

/**
 * Service Interface for managing {@link ru.georgdeveloper.myapp.domain.Profession}.
 */
public interface ProfessionService {
    /**
     * Save a profession.
     *
     * @param profession the entity to save.
     * @return the persisted entity.
     */
    Profession save(Profession profession);

    /**
     * Updates a profession.
     *
     * @param profession the entity to update.
     * @return the persisted entity.
     */
    Profession update(Profession profession);

    /**
     * Partially updates a profession.
     *
     * @param profession the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Profession> partialUpdate(Profession profession);

    /**
     * Get all the professions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Profession> findAll(Pageable pageable);

    /**
     * Get the "id" profession.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Profession> findOne(Long id);

    /**
     * Delete the "id" profession.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
