package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.JobDescription;

/**
 * Spring Data JPA repository for the JobDescription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobDescriptionRepository extends JpaRepository<JobDescription, Long> {}
