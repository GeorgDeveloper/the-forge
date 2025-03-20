package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.ProtectiveEquipment;

/**
 * Spring Data JPA repository for the ProtectiveEquipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProtectiveEquipmentRepository extends JpaRepository<ProtectiveEquipment, Long> {}
