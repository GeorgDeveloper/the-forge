package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.SafetyInstruction;
import ru.georgdeveloper.myapp.repository.SafetyInstructionRepository;
import ru.georgdeveloper.myapp.service.SafetyInstructionService;

/**
 * Service Implementation for managing {@link ru.georgdeveloper.myapp.domain.SafetyInstruction}.
 */
@Service
@Transactional
public class SafetyInstructionServiceImpl implements SafetyInstructionService {

    private static final Logger LOG = LoggerFactory.getLogger(SafetyInstructionServiceImpl.class);

    private final SafetyInstructionRepository safetyInstructionRepository;

    public SafetyInstructionServiceImpl(SafetyInstructionRepository safetyInstructionRepository) {
        this.safetyInstructionRepository = safetyInstructionRepository;
    }

    @Override
    public SafetyInstruction save(SafetyInstruction safetyInstruction) {
        LOG.debug("Request to save SafetyInstruction : {}", safetyInstruction);
        return safetyInstructionRepository.save(safetyInstruction);
    }

    @Override
    public SafetyInstruction update(SafetyInstruction safetyInstruction) {
        LOG.debug("Request to update SafetyInstruction : {}", safetyInstruction);
        return safetyInstructionRepository.save(safetyInstruction);
    }

    @Override
    public Optional<SafetyInstruction> partialUpdate(SafetyInstruction safetyInstruction) {
        LOG.debug("Request to partially update SafetyInstruction : {}", safetyInstruction);

        return safetyInstructionRepository
            .findById(safetyInstruction.getId())
            .map(existingSafetyInstruction -> {
                if (safetyInstruction.getInstructionName() != null) {
                    existingSafetyInstruction.setInstructionName(safetyInstruction.getInstructionName());
                }
                if (safetyInstruction.getIntroductionDate() != null) {
                    existingSafetyInstruction.setIntroductionDate(safetyInstruction.getIntroductionDate());
                }

                return existingSafetyInstruction;
            })
            .map(safetyInstructionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SafetyInstruction> findAll(Pageable pageable) {
        LOG.debug("Request to get all SafetyInstructions");
        return safetyInstructionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SafetyInstruction> findOne(Long id) {
        LOG.debug("Request to get SafetyInstruction : {}", id);
        return safetyInstructionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SafetyInstruction : {}", id);
        safetyInstructionRepository.deleteById(id);
    }
}
