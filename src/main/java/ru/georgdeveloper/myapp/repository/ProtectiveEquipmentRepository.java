package ru.georgdeveloper.myapp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.ProtectiveEquipment;

/**
 * Репозиторий Spring Data JPA для объекта ProtectiveEquipment.
 */
@Repository
public interface ProtectiveEquipmentRepository extends JpaRepository<ProtectiveEquipment, Long> {
    /**
     * Находит одно средство защиты с eagerly загруженными связанными сущностями.
     *
     * @param id идентификатор средства защиты
     * @return Optional со средством защиты и загруженными связями
     */
    @Query("SELECT pe FROM ProtectiveEquipment pe LEFT JOIN FETCH pe.profession WHERE pe.id = :id")
    Optional<ProtectiveEquipment> findOneWithEagerRelationships(@Param("id") Long id);

    /**
     * Находит все средства защиты с eagerly загруженными связанными сущностями.
     *
     * @return список средств защиты с загруженными связями
     */
    @Query("SELECT pe FROM ProtectiveEquipment pe LEFT JOIN FETCH pe.profession")
    List<ProtectiveEquipment> findAllWithEagerRelationships();

    /**
     * Находит все средства защиты с eagerly загруженными связанными сущностями (с пагинацией).
     *
     * @param pageable параметры пагинации
     * @return страница со средствами защиты с загруженными связями
     */
    @Query(
        value = "SELECT pe FROM ProtectiveEquipment pe LEFT JOIN FETCH pe.profession",
        countQuery = "SELECT COUNT(pe) FROM ProtectiveEquipment pe"
    )
    Page<ProtectiveEquipment> findAllWithEagerRelationships(Pageable pageable);
}
