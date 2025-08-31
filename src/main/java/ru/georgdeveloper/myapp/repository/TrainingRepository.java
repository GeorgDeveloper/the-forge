package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.Training;

/**
 * Репозиторий Spring Data JPA для обучающего объекта.
 */
@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {}
