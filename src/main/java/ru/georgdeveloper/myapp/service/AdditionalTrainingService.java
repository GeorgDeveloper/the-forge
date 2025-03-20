package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.AdditionalTraining;

/**
 * Service Interface for managing {@link ru.georgdeveloper.myapp.domain.AdditionalTraining}.
 */
public interface AdditionalTrainingService {
    /**
     * Save a additionalTraining.
     *
     * @param additionalTraining the entity to save.
     * @return the persisted entity.
     */
    AdditionalTraining save(AdditionalTraining additionalTraining);

    /**
     * Updates a additionalTraining.
     *
     * @param additionalTraining the entity to update.
     * @return the persisted entity.
     */
    AdditionalTraining update(AdditionalTraining additionalTraining);

    /**
     * Partially updates a additionalTraining.
     *
     * @param additionalTraining the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AdditionalTraining> partialUpdate(AdditionalTraining additionalTraining);

    /**
     * Get all the additionalTrainings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AdditionalTraining> findAll(Pageable pageable);

    /**
     * Get the "id" additionalTraining.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AdditionalTraining> findOne(Long id);

    /**
     * Delete the "id" additionalTraining.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
