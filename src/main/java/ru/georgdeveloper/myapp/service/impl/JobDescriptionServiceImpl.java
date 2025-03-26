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
 * Реализация сервиса для управления должностными инструкциями ({@link JobDescription}).
 * Обеспечивает CRUD операции и бизнес-логику работы с должностными инструкциями.
 */
@Service
@Transactional
public class JobDescriptionServiceImpl implements JobDescriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(JobDescriptionServiceImpl.class);

    private final JobDescriptionRepository jobDescriptionRepository;

    /**
     * Конструктор с внедрением зависимости репозитория.
     * @param jobDescriptionRepository репозиторий для работы с должностными инструкциями
     */
    public JobDescriptionServiceImpl(JobDescriptionRepository jobDescriptionRepository) {
        this.jobDescriptionRepository = jobDescriptionRepository;
    }

    /**
     * Сохраняет новую должностную инструкцию.
     * @param jobDescription сущность для сохранения
     * @return сохраненная должностная инструкция
     */
    @Override
    public JobDescription save(JobDescription jobDescription) {
        LOG.debug("Запрос на сохранение должностной инструкции: {}", jobDescription);
        return jobDescriptionRepository.save(jobDescription);
    }

    /**
     * Обновляет существующую должностную инструкцию.
     * @param jobDescription сущность с обновленными данными
     * @return обновленная должностная инструкция
     */
    @Override
    public JobDescription update(JobDescription jobDescription) {
        LOG.debug("Запрос на обновление должностной инструкции: {}", jobDescription);
        return jobDescriptionRepository.save(jobDescription);
    }

    /**
     * Частично обновляет данные должностной инструкции.
     * @param jobDescription сущность с обновляемыми полями
     * @return Optional с обновленной инструкцией, если она существует
     */
    @Override
    public Optional<JobDescription> partialUpdate(JobDescription jobDescription) {
        LOG.debug("Запрос на частичное обновление должностной инструкции: {}", jobDescription);

        return jobDescriptionRepository
            .findById(jobDescription.getId())
            .map(existingJobDescription -> {
                // Обновляем только не-null поля
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

    /**
     * Получает все должностные инструкции.
     * @return список всех должностных инструкций
     */
    @Override
    @Transactional(readOnly = true)
    public List<JobDescription> findAll() {
        LOG.debug("Запрос на получение всех должностных инструкций");
        return jobDescriptionRepository.findAll();
    }

    /**
     * Получает все должностные инструкции, не связанные с позицией.
     * @return список несвязанных должностных инструкций
     */
    @Transactional(readOnly = true)
    public List<JobDescription> findAllWherePositionIsNull() {
        LOG.debug("Запрос на получение должностных инструкций без привязки к позиции");
        return StreamSupport.stream(jobDescriptionRepository.findAll().spliterator(), false)
            .filter(jobDescription -> jobDescription.getPosition() == null)
            .toList();
    }

    /**
     * Находит должностную инструкцию по ID.
     * @param id идентификатор инструкции
     * @return Optional с найденной инструкцией
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<JobDescription> findOne(Long id) {
        LOG.debug("Запрос на получение должностной инструкции с ID: {}", id);
        return jobDescriptionRepository.findById(id);
    }

    /**
     * Удаляет должностную инструкцию по ID.
     * @param id идентификатор инструкции для удаления
     */
    @Override
    public void delete(Long id) {
        LOG.debug("Запрос на удаление должностной инструкции с ID: {}", id);
        jobDescriptionRepository.deleteById(id);
    }
}
