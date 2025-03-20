package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.Training;

/**
 * Service Interface for managing {@link ru.georgdeveloper.myapp.domain.Training}.
 */
public interface TrainingService {
    /**
     * Save a training.
     *
     * @param training the entity to save.
     * @return the persisted entity.
     */
    Training save(Training training);

    /**
     * Updates a training.
     *
     * @param training the entity to update.
     * @return the persisted entity.
     */
    Training update(Training training);

    /**
     * Partially updates a training.
     *
     * @param training the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Training> partialUpdate(Training training);

    /**
     * Get all the trainings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Training> findAll(Pageable pageable);

    /**
     * Get the "id" training.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Training> findOne(Long id);

    /**
     * Delete the "id" training.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
