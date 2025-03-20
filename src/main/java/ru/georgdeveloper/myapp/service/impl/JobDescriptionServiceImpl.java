package ru.georgdeveloper.myapp.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.JobDescription;
import ru.georgdeveloper.myapp.repository.JobDescriptionRepository;
import ru.georgdeveloper.myapp.service.JobDescriptionService;

/**
 * Service Implementation for managing {@link ru.georgdeveloper.myapp.domain.JobDescription}.
 */
@Service
@Transactional
public class JobDescriptionServiceImpl implements JobDescriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(JobDescriptionServiceImpl.class);

    private final JobDescriptionRepository jobDescriptionRepository;

    public JobDescriptionServiceImpl(JobDescriptionRepository jobDescriptionRepository) {
        this.jobDescriptionRepository = jobDescriptionRepository;
    }

    @Override
    public JobDescription save(JobDescription jobDescription) {
        LOG.debug("Request to save JobDescription : {}", jobDescription);
        return jobDescriptionRepository.save(jobDescription);
    }

    @Override
    public JobDescription update(JobDescription jobDescription) {
        LOG.debug("Request to update JobDescription : {}", jobDescription);
        return jobDescriptionRepository.save(jobDescription);
    }

    @Override
    public Optional<JobDescription> partialUpdate(JobDescription jobDescription) {
        LOG.debug("Request to partially update JobDescription : {}", jobDescription);

        return jobDescriptionRepository
            .findById(jobDescription.getId())
            .map(existingJobDescription -> {
                if (jobDescription.getDescriptionName() != null) {
                    existingJobDescription.setDescriptionName(jobDescription.getDescriptionName());
                }
                if (jobDescription.getApprovalDate() != null) {
                    existingJobDescription.setApprovalDate(jobDescription.getApprovalDate());
                }

                return existingJobDescription;
            })
            .map(jobDescriptionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobDescription> findAll() {
        LOG.debug("Request to get all JobDescriptions");
        return jobDescriptionRepository.findAll();
    }

    /**
     *  Get all the jobDescriptions where Position is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<JobDescription> findAllWherePositionIsNull() {
        LOG.debug("Request to get all jobDescriptions where Position is null");
        return StreamSupport.stream(jobDescriptionRepository.findAll().spliterator(), false)
            .filter(jobDescription -> jobDescription.getPosition() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JobDescription> findOne(Long id) {
        LOG.debug("Request to get JobDescription : {}", id);
        return jobDescriptionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete JobDescription : {}", id);
        jobDescriptionRepository.deleteById(id);
    }
}
