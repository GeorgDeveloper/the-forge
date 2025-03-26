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
 * Реализация сервиса для управления обучением сотрудников.
 * Предоставляет CRUD-операции для сущности {@link ru.georgdeveloper.myapp.domain.Training}.
 */
@Service // Указывает, что класс является Spring-сервисом
@Transactional // Обеспечивает транзакционность всех методов
public class TrainingServiceImpl implements TrainingService {

    // Логгер для записи информации о работе сервиса
    private static final Logger LOG = LoggerFactory.getLogger(TrainingServiceImpl.class);

    // Репозиторий для работы с данными инструктажей в БД
    private final TrainingRepository trainingRepository;

    /**
     * Конструктор с внедрением зависимости TrainingRepository
     * @param trainingRepository - репозиторий для работы с инструктажами
     */
    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    /**
     * Сохраняет новый курс инструктажа
     * @param training - сущность инструктажа для сохранения
     * @return сохраненная сущность Training
     */
    @Override
    public Training save(Training training) {
        LOG.debug("Запрос на сохранение инструктажа: {}", training);
        return trainingRepository.save(training);
    }

    /**
     * Полностью обновляет данные инструктажа
     * @param training - сущность с обновленными данными
     * @return обновленная сущность Training
     */
    @Override
    public Training update(Training training) {
        LOG.debug("Запрос на обновление инструктажа: {}", training);
        return trainingRepository.save(training);
    }

    /**
     * Частично обновляет данные инструктажа
     * @param training - сущность с полями для обновления
     * @return Optional с обновленным обучением, если запись найдена
     */
    @Override
    public Optional<Training> partialUpdate(Training training) {
        LOG.debug("Запрос на частичное обновление инструктажа: {}", training);

        return trainingRepository
            .findById(training.getId())
            .map(existingTraining -> {
                // Последовательное обновление полей, если они указаны
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

    /**
     * Получает список всех курсов инструктажа с пагинацией
     * @param pageable - параметры пагинации
     * @return страница с инструктажом
     */
    @Override
    @Transactional(readOnly = true) // Оптимизация для операций чтения
    public Page<Training> findAll(Pageable pageable) {
        LOG.debug("Запрос на получение всех курсов инструктажа");
        return trainingRepository.findAll(pageable);
    }

    /**
     * Находит курс обучения по ID
     * @param id - идентификатор обучения
     * @return Optional с найденным обучением или пустой
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Training> findOne(Long id) {
        LOG.debug("Запрос на получение инструктажа по ID: {}", id);
        return trainingRepository.findById(id);
    }

    /**
     * Удаляет курс инструктажа по ID
     * @param id - идентификатор инструктажа для удаления
     */
    @Override
    public void delete(Long id) {
        LOG.debug("Запрос на удаление инструктажа с ID: {}", id);
        trainingRepository.deleteById(id);
    }
}
