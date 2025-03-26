package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.JobDescription;

/**
 * Репозиторий Spring Data JPA для объекта JobDescription.
 */
@SuppressWarnings("unused")
@Repository
public interface JobDescriptionRepository extends JpaRepository<JobDescription, Long> {}
