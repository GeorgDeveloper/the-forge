package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.AdditionalTraining;

/**
 * Spring Data JPA repository for the AdditionalTraining entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdditionalTrainingRepository extends JpaRepository<AdditionalTraining, Long> {}
