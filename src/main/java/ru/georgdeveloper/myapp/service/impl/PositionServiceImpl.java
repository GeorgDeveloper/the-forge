package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Position;
import ru.georgdeveloper.myapp.repository.PositionRepository;
import ru.georgdeveloper.myapp.service.PositionService;

/**
 * Реализация сервиса для управления сущностью {@link Position}.
 * Обеспечивает CRUD операции и бизнес-логику работы с должностями сотрудников.
 */
@Service
@Transactional
public class PositionServiceImpl implements PositionService {

    private static final Logger LOG = LoggerFactory.getLogger(PositionServiceImpl.class);

    private final PositionRepository positionRepository;

    /**
     * Конструктор с внедрением зависимости репозитория.
     * @param positionRepository репозиторий для работы с должностями
     */
    public PositionServiceImpl(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    /**
     * Сохраняет новую должность.
     * @param position сущность для сохранения
     * @return сохраненная должность
     */
    @Override
    public Position save(Position position) {
        LOG.debug("Запрос на сохранение должности: {}", position);
        return positionRepository.save(position);
    }

    /**
     * Обновляет существующую должность.
     * @param position сущность с обновленными данными
     * @return обновленная должность
     */
    @Override
    public Position update(Position position) {
        LOG.debug("Запрос на обновление должности: {}", position);
        return positionRepository.save(position);
    }

    /**
     * Частично обновляет данные должности.
     * @param position сущность с обновляемыми полями
     * @return Optional с обновленной должностью, если она существует
     */
    @Override
    public Optional<Position> partialUpdate(Position position) {
        LOG.debug("Запрос на частичное обновление должности: {}", position);

        return positionRepository
            .findById(position.getId())
            .map(existingPosition -> {
                // Обновляем только не-null поля
                if (position.getPositionName() != null) {
                    existingPosition.setPositionName(position.getPositionName());
                }

                return existingPosition;
            })
            .map(positionRepository::save);
    }

    /**
     * Получает все должности с пагинацией.
     * @param pageable параметры пагинации
     * @return страница с должностями
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Position> findAll(Pageable pageable) {
        LOG.debug("Запрос на получение всех должностей");
        return positionRepository.findAll(pageable);
    }

    /**
     * Находит должность по ID.
     * @param id идентификатор должности
     * @return Optional с найденной должностью
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Position> findOne(Long id) {
        LOG.debug("Запрос на получение должности с ID: {}", id);
        return positionRepository.findById(id);
    }

    /**
     * Удаляет должность по ID.
     * @param id идентификатор должности для удаления
     */
    @Override
    public void delete(Long id) {
        LOG.debug("Запрос на удаление должности с ID: {}", id);
        positionRepository.deleteById(id);
    }
}
