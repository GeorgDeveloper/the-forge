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
 * Реализация сервиса для управления сущностью {@link AdditionalTraining}.
 * Обеспечивает CRUD операции и бизнес-логику работы с дополнительными обучениями.
 */
@Service
@Transactional
public class AdditionalTrainingServiceImpl implements AdditionalTrainingService {

    private static final Logger LOG = LoggerFactory.getLogger(AdditionalTrainingServiceImpl.class);

    private final AdditionalTrainingRepository additionalTrainingRepository;

    /**
     * Конструктор с внедрением зависимости репозитория.
     * @param additionalTrainingRepository репозиторий для работы с AdditionalTraining
     */
    public AdditionalTrainingServiceImpl(AdditionalTrainingRepository additionalTrainingRepository) {
        this.additionalTrainingRepository = additionalTrainingRepository;
    }

    /**
     * Сохраняет новое дополнительное обучение.
     * @param additionalTraining сущность для сохранения
     * @return сохраненная сущность
     */
    @Override
    public AdditionalTraining save(AdditionalTraining additionalTraining) {
        LOG.debug("Запрос на сохранение AdditionalTraining : {}", additionalTraining);
        return additionalTrainingRepository.save(additionalTraining);
    }

    /**
     * Обновляет существующее дополнительное обучение.
     * @param additionalTraining сущность для обновления
     * @return обновленная сущность
     */
    @Override
    public AdditionalTraining update(AdditionalTraining additionalTraining) {
        LOG.debug("Запрос на обновление AdditionalTraining : {}", additionalTraining);
        return additionalTrainingRepository.save(additionalTraining);
    }

    /**
     * Частично обновляет существующее дополнительное обучение.
     * @param additionalTraining сущность с обновляемыми полями
     * @return Optional с обновленной сущностью, если она существует
     */
    @Override
    public Optional<AdditionalTraining> partialUpdate(AdditionalTraining additionalTraining) {
        LOG.debug("Запрос на частичное обновление AdditionalTraining : {}", additionalTraining);

        return additionalTrainingRepository
            .findById(additionalTraining.getId())
            .map(existingAdditionalTraining -> {
                // Обновляем только не-null поля
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

    /**
     * Получает все дополнительные обучения с пагинацией.
     * @param pageable параметры пагинации
     * @return страница с дополнительными обучениями
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdditionalTraining> findAll(Pageable pageable) {
        LOG.debug("Запрос на получение всех AdditionalTrainings");
        return additionalTrainingRepository.findAll(pageable);
    }

    /**
     * Получает одно дополнительное обучение по ID.
     * @param id идентификатор обучения
     * @return Optional с найденным обучением
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AdditionalTraining> findOne(Long id) {
        LOG.debug("Запрос на получение AdditionalTraining : {}", id);
        return additionalTrainingRepository.findById(id);
    }

    /**
     * Удаляет дополнительное обучение по ID.
     * @param id идентификатор обучения для удаления
     */
    @Override
    public void delete(Long id) {
        LOG.debug("Запрос на удаление AdditionalTraining : {}", id);
        additionalTrainingRepository.deleteById(id);
    }
}
