package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.AdditionalTraining;

/**
 * Репозиторий Spring Data JPA для объекта AdditionalTraining.
 */
@SuppressWarnings("unused")
@Repository
public interface AdditionalTrainingRepository extends JpaRepository<AdditionalTraining, Long> {}
