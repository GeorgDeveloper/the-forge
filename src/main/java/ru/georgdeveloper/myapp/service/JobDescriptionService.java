package ru.georgdeveloper.myapp.service;

import java.util.List;
import java.util.Optional;
import ru.georgdeveloper.myapp.domain.JobDescription;

/**
 * Service Interface for managing {@link ru.georgdeveloper.myapp.domain.JobDescription}.
 */
public interface JobDescriptionService {
    /**
     * Save a jobDescription.
     *
     * @param jobDescription the entity to save.
     * @return the persisted entity.
     */
    JobDescription save(JobDescription jobDescription);

    /**
     * Updates a jobDescription.
     *
     * @param jobDescription the entity to update.
     * @return the persisted entity.
     */
    JobDescription update(JobDescription jobDescription);

    /**
     * Partially updates a jobDescription.
     *
     * @param jobDescription the entity to update partially.
     * @return the persisted entity.
     */
    Optional<JobDescription> partialUpdate(JobDescription jobDescription);

    /**
     * Get all the jobDescriptions.
     *
     * @return the list of entities.
     */
    List<JobDescription> findAll();

    /**
     * Get all the JobDescription where Position is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<JobDescription> findAllWherePositionIsNull();

    /**
     * Get the "id" jobDescription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JobDescription> findOne(Long id);

    /**
     * Delete the "id" jobDescription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
