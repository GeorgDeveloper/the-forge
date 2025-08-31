package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.Authority;

/**
 *  Репозиторий Spring Data JPA для Authority.
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
