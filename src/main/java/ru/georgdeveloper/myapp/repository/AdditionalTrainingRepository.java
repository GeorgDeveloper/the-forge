package ru.georgdeveloper.myapp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.AdditionalTraining;

/**
 * Репозиторий Spring Data JPA для объекта AdditionalTraining.
 */
@Repository
public interface AdditionalTrainingRepository extends JpaRepository<AdditionalTraining, Long> {
    /**
     * Находит одно дополнительное обучение с eagerly загруженными связанными сущностями.
     *
     * @param id идентификатор дополнительного обучения
     * @return Optional с дополнительным обучением и загруженными связями
     */
    @Query("SELECT at FROM AdditionalTraining at LEFT JOIN FETCH at.profession WHERE at.id = :id")
    Optional<AdditionalTraining> findOneWithEagerRelationships(@Param("id") Long id);

    /**
     * Находит все дополнительные обучения с eagerly загруженными связанными сущностями.
     *
     * @return список дополнительных обучений с загруженными связями
     */
    @Query("SELECT at FROM AdditionalTraining at LEFT JOIN FETCH at.profession")
    List<AdditionalTraining> findAllWithEagerRelationships();

    /**
     * Находит все дополнительные обучения с eagerly загруженными связанными сущностями (с пагинацией).
     *
     * @param pageable параметры пагинации
     * @return страница с дополнительными обучениями с загруженными связями
     */
    @Query(
        value = "SELECT at FROM AdditionalTraining at LEFT JOIN FETCH at.profession",
        countQuery = "SELECT COUNT(at) FROM AdditionalTraining at"
    )
    Page<AdditionalTraining> findAllWithEagerRelationships(Pageable pageable);
}
