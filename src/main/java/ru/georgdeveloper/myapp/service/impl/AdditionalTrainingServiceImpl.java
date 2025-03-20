package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.AdditionalTraining;
import ru.georgdeveloper.myapp.repository.AdditionalTrainingRepository;
import ru.georgdeveloper.myapp.service.AdditionalTrainingService;

/**
 * Service Implementation for managing {@link ru.georgdeveloper.myapp.domain.AdditionalTraining}.
 */
@Service
@Transactional
public class AdditionalTrainingServiceImpl implements AdditionalTrainingService {

    private static final Logger LOG = LoggerFactory.getLogger(AdditionalTrainingServiceImpl.class);

    private final AdditionalTrainingRepository additionalTrainingRepository;

    public AdditionalTrainingServiceImpl(AdditionalTrainingRepository additionalTrainingRepository) {
        this.additionalTrainingRepository = additionalTrainingRepository;
    }

    @Override
    public AdditionalTraining save(AdditionalTraining additionalTraining) {
        LOG.debug("Request to save AdditionalTraining : {}", additionalTraining);
        return additionalTrainingRepository.save(additionalTraining);
    }

    @Override
    public AdditionalTraining update(AdditionalTraining additionalTraining) {
        LOG.debug("Request to update AdditionalTraining : {}", additionalTraining);
        return additionalTrainingRepository.save(additionalTraining);
    }

    @Override
    public Optional<AdditionalTraining> partialUpdate(AdditionalTraining additionalTraining) {
        LOG.debug("Request to partially update AdditionalTraining : {}", additionalTraining);

        return additionalTrainingRepository
            .findById(additionalTraining.getId())
            .map(existingAdditionalTraining -> {
                if (additionalTraining.getTrainingName() != null) {
                    existingAdditionalTraining.setTrainingName(additionalTraining.getTrainingName());
                }
                if (additionalTraining.getTrainingDate() != null) {
                    existingAdditionalTraining.setTrainingDate(additionalTraining.getTrainingDate());
                }
                if (additionalTraining.getValidityPeriod() != null) {
                    existingAdditionalTraining.setValidityPeriod(additionalTraining.getValidityPeriod());
                }
                if (additionalTraining.getNextTrainingDate() != null) {
                    existingAdditionalTraining.setNextTrainingDate(additionalTraining.getNextTrainingDate());
                }

                return existingAdditionalTraining;
            })
            .map(additionalTrainingRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdditionalTraining> findAll(Pageable pageable) {
        LOG.debug("Request to get all AdditionalTrainings");
        return additionalTrainingRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdditionalTraining> findOne(Long id) {
        LOG.debug("Request to get AdditionalTraining : {}", id);
        return additionalTrainingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AdditionalTraining : {}", id);
        additionalTrainingRepository.deleteById(id);
    }
}
