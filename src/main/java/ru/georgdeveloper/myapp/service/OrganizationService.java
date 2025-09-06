package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import ru.georgdeveloper.myapp.domain.Organization;

public interface OrganizationService {
    Organization save(Organization organization);

    Organization update(Organization organization);

    Optional<Organization> partialUpdate(Organization organization);

    void delete(Long id);
}
