package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.ProcedureDocument;

/**
 * Spring Data JPA репозиторий для сущности {@link ProcedureDocument}.
 */
@Repository
public interface ProcedureDocumentRepository extends JpaRepository<ProcedureDocument, Long> {}
