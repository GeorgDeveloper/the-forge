package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.ProtectiveEquipment;

/**
 * Репозиторий Spring Data JPA для объекта Protectivequipment.
 */
@SuppressWarnings("unused")
@Repository
public interface ProtectiveEquipmentRepository extends JpaRepository<ProtectiveEquipment, Long> {}
