package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Training;
import ru.georgdeveloper.myapp.repository.TrainingRepository;
import ru.georgdeveloper.myapp.service.TrainingService;

/**
 * Service Implementation for managing {@link ru.georgdeveloper.myapp.domain.Training}.
 */
@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingRepository trainingRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Training save(Training training) {
        LOG.debug("Request to save Training : {}", training);
        return trainingRepository.save(training);
    }

    @Override
    public Training update(Training training) {
        LOG.debug("Request to update Training : {}", training);
        return trainingRepository.save(training);
    }

    @Override
    public Optional<Training> partialUpdate(Training training) {
        LOG.debug("Request to partially update Training : {}", training);

        return trainingRepository
            .findById(training.getId())
            .map(existingTraining -> {
                if (training.getTrainingName() != null) {
                    existingTraining.setTrainingName(training.getTrainingName());
                }
                if (training.getLastTrainingDate() != null) {
                    existingTraining.setLastTrainingDate(training.getLastTrainingDate());
                }
                if (training.getValidityPeriod() != null) {
                    existingTraining.setValidityPeriod(training.getValidityPeriod());
                }
                if (training.getNextTrainingDate() != null) {
                    existingTraining.setNextTrainingDate(training.getNextTrainingDate());
                }

                return existingTraining;
            })
            .map(trainingRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Training> findAll(Pageable pageable) {
        LOG.debug("Request to get all Trainings");
        return trainingRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Training> findOne(Long id) {
        LOG.debug("Request to get Training : {}", id);
        return trainingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Training : {}", id);
        trainingRepository.deleteById(id);
    }
}
