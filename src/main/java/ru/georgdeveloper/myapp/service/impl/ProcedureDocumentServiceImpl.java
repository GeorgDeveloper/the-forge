package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.ProcedureDocument;
import ru.georgdeveloper.myapp.repository.ProcedureDocumentRepository;
import ru.georgdeveloper.myapp.service.ProcedureDocumentService;

/**
 * Реализация сервиса для управления сущностью {@link ProcedureDocument}.
 */
@Service
@Transactional
public class ProcedureDocumentServiceImpl implements ProcedureDocumentService {

    private final Logger log = LoggerFactory.getLogger(ProcedureDocumentServiceImpl.class);

    private final ProcedureDocumentRepository procedureDocumentRepository;

    public ProcedureDocumentServiceImpl(ProcedureDocumentRepository procedureDocumentRepository) {
        this.procedureDocumentRepository = procedureDocumentRepository;
    }

    @Override
    public ProcedureDocument save(ProcedureDocument procedureDocument) {
        log.debug("Запрос на сохранение процедуры и документации: {}", procedureDocument);
        return procedureDocumentRepository.save(procedureDocument);
    }

    @Override
    public ProcedureDocument update(ProcedureDocument procedureDocument) {
        log.debug("Запрос на обновление процедуры и документации: {}", procedureDocument);
        return procedureDocumentRepository.save(procedureDocument);
    }

    @Override
    public Optional<ProcedureDocument> partialUpdate(ProcedureDocument procedureDocument) {
        log.debug("Запрос на частичное обновление процедуры и документации: {}", procedureDocument);

        return procedureDocumentRepository
            .findById(procedureDocument.getId())
            .map(existingProcedureDocument -> {
                if (procedureDocument.getDocumentName() != null) {
                    existingProcedureDocument.setDocumentName(procedureDocument.getDocumentName());
                }
                if (procedureDocument.getIntroductionDate() != null) {
                    existingProcedureDocument.setIntroductionDate(procedureDocument.getIntroductionDate());
                }
                if (procedureDocument.getDescription() != null) {
                    existingProcedureDocument.setDescription(procedureDocument.getDescription());
                }
                if (procedureDocument.getPdfFileName() != null) {
                    existingProcedureDocument.setPdfFileName(procedureDocument.getPdfFileName());
                }
                if (procedureDocument.getPdfFileContentType() != null) {
                    existingProcedureDocument.setPdfFileContentType(procedureDocument.getPdfFileContentType());
                }
                if (procedureDocument.getPdfFile() != null) {
                    existingProcedureDocument.setPdfFile(procedureDocument.getPdfFile());
                }
                if (procedureDocument.getProfession() != null) {
                    existingProcedureDocument.setProfession(procedureDocument.getProfession());
                }
                if (procedureDocument.getPosition() != null) {
                    existingProcedureDocument.setPosition(procedureDocument.getPosition());
                }

                return existingProcedureDocument;
            })
            .map(procedureDocumentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcedureDocument> findAll(Pageable pageable) {
        log.debug("Запрос на получение всех процедур и документаций");
        return procedureDocumentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcedureDocument> findOne(Long id) {
        log.debug("Запрос на получение процедуры и документации: {}", id);
        return procedureDocumentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Запрос на удаление процедуры и документации: {}", id);
        procedureDocumentRepository.deleteById(id);
    }
}
