package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {}
