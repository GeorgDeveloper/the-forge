package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.Position;

/**
 * Service Interface for managing {@link ru.georgdeveloper.myapp.domain.Position}.
 */
public interface PositionService {
    /**
     * Save a position.
     *
     * @param position the entity to save.
     * @return the persisted entity.
     */
    Position save(Position position);

    /**
     * Updates a position.
     *
     * @param position the entity to update.
     * @return the persisted entity.
     */
    Position update(Position position);

    /**
     * Partially updates a position.
     *
     * @param position the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Position> partialUpdate(Position position);

    /**
     * Get all the positions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Position> findAll(Pageable pageable);

    /**
     * Get the "id" position.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Position> findOne(Long id);

    /**
     * Delete the "id" position.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
