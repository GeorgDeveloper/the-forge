package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.ProtectiveEquipment;

/**
 * Service Interface for managing {@link ru.georgdeveloper.myapp.domain.ProtectiveEquipment}.
 */
public interface ProtectiveEquipmentService {
    /**
     * Save a protectiveEquipment.
     *
     * @param protectiveEquipment the entity to save.
     * @return the persisted entity.
     */
    ProtectiveEquipment save(ProtectiveEquipment protectiveEquipment);

    /**
     * Updates a protectiveEquipment.
     *
     * @param protectiveEquipment the entity to update.
     * @return the persisted entity.
     */
    ProtectiveEquipment update(ProtectiveEquipment protectiveEquipment);

    /**
     * Partially updates a protectiveEquipment.
     *
     * @param protectiveEquipment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProtectiveEquipment> partialUpdate(ProtectiveEquipment protectiveEquipment);

    /**
     * Get all the protectiveEquipments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProtectiveEquipment> findAll(Pageable pageable);

    /**
     * Get the "id" protectiveEquipment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProtectiveEquipment> findOne(Long id);

    /**
     * Delete the "id" protectiveEquipment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
