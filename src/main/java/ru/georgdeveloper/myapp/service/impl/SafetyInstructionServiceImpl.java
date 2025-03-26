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
 * Реализация сервиса для управления инструкциями по безопасности.
 * Обеспечивает основные CRUD-операции для сущности {@link ru.georgdeveloper.myapp.domain.SafetyInstruction}.
 */
@Service
@Transactional // Все методы класса выполняются в транзакции
public class SafetyInstructionServiceImpl implements SafetyInstructionService {

    // Логгер для записи сообщений о работе сервиса
    private static final Logger LOG = LoggerFactory.getLogger(SafetyInstructionServiceImpl.class);

    // Репозиторий для работы с базой данных
    private final SafetyInstructionRepository safetyInstructionRepository;

    /**
     * Конструктор с внедрением зависимости SafetyInstructionRepository
     * @param safetyInstructionRepository - репозиторий для работы с инструкциями
     */
    public SafetyInstructionServiceImpl(SafetyInstructionRepository safetyInstructionRepository) {
        this.safetyInstructionRepository = safetyInstructionRepository;
    }

    /**
     * Сохраняет новую инструкцию по безопасности
     * @param safetyInstruction - сущность инструкции для сохранения
     * @return сохраненная сущность SafetyInstruction
     */
    @Override
    public SafetyInstruction save(SafetyInstruction safetyInstruction) {
        LOG.debug("Запрос на сохранение инструкции по безопасности: {}", safetyInstruction);
        return safetyInstructionRepository.save(safetyInstruction);
    }

    /**
     * Полностью обновляет существующую инструкцию
     * @param safetyInstruction - сущность с обновленными данными
     * @return обновленная сущность SafetyInstruction
     */
    @Override
    public SafetyInstruction update(SafetyInstruction safetyInstruction) {
        LOG.debug("Запрос на обновление инструкции по безопасности: {}", safetyInstruction);
        return safetyInstructionRepository.save(safetyInstruction);
    }

    /**
     * Частично обновляет инструкцию (только указанные поля)
     * @param safetyInstruction - сущность с полями для обновления
     * @return Optional с обновленной сущностью, если инструкция найдена
     */
    @Override
    public Optional<SafetyInstruction> partialUpdate(SafetyInstruction safetyInstruction) {
        LOG.debug("Запрос на частичное обновление инструкции: {}", safetyInstruction);

        return safetyInstructionRepository
            .findById(safetyInstruction.getId())
            .map(existingSafetyInstruction -> {
                // Обновляем название инструкции, если оно указано
                if (safetyInstruction.getInstructionName() != null) {
                    existingSafetyInstruction.setInstructionName(safetyInstruction.getInstructionName());
                }
                // Обновляем дату введения, если она указана
                if (safetyInstruction.getIntroductionDate() != null) {
                    existingSafetyInstruction.setIntroductionDate(safetyInstruction.getIntroductionDate());
                }

                return existingSafetyInstruction;
            })
            .map(safetyInstructionRepository::save);
    }

    /**
     * Получает все инструкции с поддержкой пагинации
     * @param pageable - параметры пагинации (номер страницы, размер и т.д.)
     * @return страница с инструкциями
     */
    @Override
    @Transactional(readOnly = true) // Оптимизация для операций чтения
    public Page<SafetyInstruction> findAll(Pageable pageable) {
        LOG.debug("Запрос на получение всех инструкций по безопасности");
        return safetyInstructionRepository.findAll(pageable);
    }

    /**
     * Находит инструкцию по идентификатору
     * @param id - уникальный идентификатор инструкции
     * @return Optional с найденной инструкцией или пустой, если не найдена
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SafetyInstruction> findOne(Long id) {
        LOG.debug("Запрос на получение инструкции по ID: {}", id);
        return safetyInstructionRepository.findById(id);
    }

    /**
     * Удаляет инструкцию по идентификатору
     * @param id - уникальный идентификатор инструкции для удаления
     */
    @Override
    public void delete(Long id) {
        LOG.debug("Запрос на удаление инструкции с ID: {}", id);
        safetyInstructionRepository.deleteById(id);
    }
}
