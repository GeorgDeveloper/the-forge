package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.ProtectiveEquipment;
import ru.georgdeveloper.myapp.repository.ProtectiveEquipmentRepository;
import ru.georgdeveloper.myapp.service.ProtectiveEquipmentService;

/**
 * Реализация сервиса для управления средствами индивидуальной защиты (СИЗ).
 * Обеспечивает основные CRUD-операции для сущности {@link ru.georgdeveloper.myapp.domain.ProtectiveEquipment}.
 */
@Service // Аннотация указывает, что это Spring-сервис (компонент бизнес-логики)
@Transactional // Все методы класса будут выполняться в транзакции по умолчанию
public class ProtectiveEquipmentServiceImpl implements ProtectiveEquipmentService {

    // Логгер для записи сообщений
    private static final Logger LOG = LoggerFactory.getLogger(ProtectiveEquipmentServiceImpl.class);

    // Репозиторий для работы с ProtectiveEquipment в базе данных
    private final ProtectiveEquipmentRepository protectiveEquipmentRepository;

    // Конструктор с внедрением зависимости ProtectiveEquipmentRepository
    public ProtectiveEquipmentServiceImpl(ProtectiveEquipmentRepository protectiveEquipmentRepository) {
        this.protectiveEquipmentRepository = protectiveEquipmentRepository;
    }

    /**
     * Сохраняет средство защиты в базе данных.
     * @param protectiveEquipment - сущность ProtectiveEquipment для сохранения
     * @return сохраненная сущность ProtectiveEquipment
     */
    @Override
    public ProtectiveEquipment save(ProtectiveEquipment protectiveEquipment) {
        LOG.debug("Запрос на сохранение средства защиты: {}", protectiveEquipment);
        return protectiveEquipmentRepository.save(protectiveEquipment);
    }

    /**
     * Обновляет существующее средство защиты в базе данных.
     * @param protectiveEquipment - сущность ProtectiveEquipment с обновленными данными
     * @return обновленная сущность ProtectiveEquipment
     */
    @Override
    public ProtectiveEquipment update(ProtectiveEquipment protectiveEquipment) {
        LOG.debug("Запрос на обновление средства защиты: {}", protectiveEquipment);
        return protectiveEquipmentRepository.save(protectiveEquipment);
    }

    /**
     * Частично обновляет средство защиты (только измененные поля).
     * @param protectiveEquipment - сущность ProtectiveEquipment с обновляемыми полями
     * @return Optional с обновленной сущностью, если средство защиты найдено
     */
    @Override
    public Optional<ProtectiveEquipment> partialUpdate(ProtectiveEquipment protectiveEquipment) {
        LOG.debug("Запрос на частичное обновление средства защиты: {}", protectiveEquipment);

        return protectiveEquipmentRepository
            .findById(protectiveEquipment.getId())
            .map(existingProtectiveEquipment -> {
                // Обновляем только те поля, которые указаны в запросе
                if (protectiveEquipment.getEquipmentName() != null) {
                    existingProtectiveEquipment.setEquipmentName(protectiveEquipment.getEquipmentName());
                }
                if (protectiveEquipment.getQuantity() != null) {
                    existingProtectiveEquipment.setQuantity(protectiveEquipment.getQuantity());
                }
                if (protectiveEquipment.getIssuanceFrequency() != null) {
                    existingProtectiveEquipment.setIssuanceFrequency(protectiveEquipment.getIssuanceFrequency());
                }

                return existingProtectiveEquipment;
            })
            .map(protectiveEquipmentRepository::save);
    }

    /**
     * Получает все средства защиты с пагинацией.
     * @param pageable - параметры пагинации
     * @return страница со средствами защиты
     */
    @Override
    @Transactional(readOnly = true) // Только для чтения, без изменений в БД
    public Page<ProtectiveEquipment> findAll(Pageable pageable) {
        LOG.debug("Запрос на получение всех средств защиты");
        return protectiveEquipmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Находит средство защиты по идентификатору.
     * @param id - идентификатор средства защиты
     * @return Optional с найденным средством защиты или пустой, если не найдено
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProtectiveEquipment> findOne(Long id) {
        LOG.debug("Запрос на получение средства защиты по ID: {}", id);
        return protectiveEquipmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Удаляет средство защиты по идентификатору.
     * @param id - идентификатор средства защиты для удаления
     */
    @Override
    public void delete(Long id) {
        LOG.debug("Запрос на удаление средства защиты с ID: {}", id);
        protectiveEquipmentRepository.deleteById(id);
    }
}
