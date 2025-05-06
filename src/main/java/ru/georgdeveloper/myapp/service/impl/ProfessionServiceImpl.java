package ru.georgdeveloper.myapp.service.impl;

import java.util.HashSet;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Employee;
import ru.georgdeveloper.myapp.domain.Profession;
import ru.georgdeveloper.myapp.repository.EmployeeRepository;
import ru.georgdeveloper.myapp.repository.ProfessionRepository;
import ru.georgdeveloper.myapp.service.ProfessionService;

/**
 * Реализация сервиса для управления сущностью {@link Profession}.
 * Обеспечивает основные CRUD-операции для профессий.
 */
@Service // Аннотация указывает, что это Spring-сервис (компонент бизнес-логики)
@Transactional // Все методы класса будут выполняться в транзакции по умолчанию
public class ProfessionServiceImpl implements ProfessionService {

    // Логгер для записи сообщений
    private static final Logger LOG = LoggerFactory.getLogger(ProfessionServiceImpl.class);

    // Репозиторий для работы с Profession в базе данных
    private final ProfessionRepository professionRepository;
    private final EmployeeRepository employeeRepository;

    // Конструктор с внедрением зависимости ProfessionRepository
    public ProfessionServiceImpl(ProfessionRepository professionRepository, EmployeeRepository employeeRepository) {
        this.professionRepository = professionRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Сохраняет профессию в базе данных.
     * @param profession - сущность Profession для сохранения
     * @return сохраненная сущность Profession
     */
    @Override
    public Profession save(Profession profession) {
        LOG.debug("Request to save Profession : {}", profession);

        // Сохраняем профессию сначала без связей
        Profession savedProfession = professionRepository.save(profession);

        // Затем обновляем связи с сотрудниками
        if (savedProfession.getEmployees() != null) {
            for (Employee employee : savedProfession.getEmployees()) {
                // Загружаем полную версию сотрудника из БД
                Employee managedEmployee = employeeRepository.findById(employee.getId()).orElseThrow();

                // Инициализируем коллекции, если нужно
                if (managedEmployee.getProfessions() == null) {
                    managedEmployee.setProfessions(new HashSet<>());
                }

                // Добавляем профессию сотруднику
                managedEmployee.getProfessions().add(savedProfession);
                employeeRepository.save(managedEmployee);
            }
        }

        return savedProfession;
    }

    /**
     * Обновляет существующую профессию в базе данных.
     * @param profession - сущность Profession с обновленными данными
     * @return обновленная сущность Profession
     */
    @Override
    public Profession update(Profession profession) {
        LOG.debug("Request to update Profession : {}", profession);
        return save(profession);
    }

    /**
     * Частично обновляет профессию (только измененные поля).
     * @param profession - сущность Profession с обновляемыми полями
     * @return Optional с обновленной сущностью, если профессия найдена
     */
    @Override
    public Optional<Profession> partialUpdate(Profession profession) {
        LOG.debug("Request to partially update Profession : {}", profession);

        return professionRepository
            .findById(profession.getId())
            .map(existingProfession -> {
                // Обновляем только professionName, если он указан в запросе
                if (profession.getProfessionName() != null) {
                    existingProfession.setProfessionName(profession.getProfessionName());
                }
                if (profession.getEmployees() != null) {
                    existingProfession.getEmployees().addAll(profession.getEmployees());
                }

                return existingProfession;
            })
            .map(m -> save(m));
    }

    /**
     * Получает все профессии с пагинацией.
     * @param pageable - параметры пагинации
     * @return страница с профессиями
     */
    @Override
    @Transactional(readOnly = true) // Только для чтения, без изменений в БД
    public Page<Profession> findAll(Pageable pageable) {
        LOG.debug("Request to get all Professions");
        return professionRepository.findAll(pageable);
    }

    /**
     * Находит профессию по идентификатору.
     * @param id - идентификатор профессии
     * @return Optional с найденной профессией или пустой, если не найдена
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Profession> findOne(Long id) {
        LOG.debug("Request to get Profession : {}", id);

        return professionRepository.findWithEmployeesById(id);
    }

    /**
     * Удаляет профессию по идентификатору.
     * @param id - идентификатор профессии для удаления
     */
    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Profession : {}", id);
        professionRepository.deleteById(id);
    }
}
