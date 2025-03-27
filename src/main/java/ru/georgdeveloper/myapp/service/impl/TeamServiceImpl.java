package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Team;
import ru.georgdeveloper.myapp.repository.TeamRepository;
import ru.georgdeveloper.myapp.service.TeamService;

/**
 * Реализация сервиса для управления командами.
 * Обеспечивает базовые CRUD-операции для сущности {@link ru.georgdeveloper.myapp.domain.Team}.
 */
@Service // Аннотация Spring, обозначающая класс как сервисный компонент
@Transactional // Все методы выполняются в транзакционном контексте
public class TeamServiceImpl implements TeamService {

    // Логгер для записи информации о работе сервиса
    private static final Logger LOG = LoggerFactory.getLogger(TeamServiceImpl.class);

    // Репозиторий для работы с данными команд в БД
    private final TeamRepository teamRepository;

    /**
     * Конструктор с внедрением зависимости TeamRepository
     * @param teamRepository - репозиторий для работы с командами
     */
    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * Сохраняет новую команду в системе
     * @param team - сущность команды для сохранения
     * @return сохраненная сущность Team
     */
    @Override
    public Team save(Team team) {
        LOG.debug("Запрос на сохранение команды: {}", team);
        return teamRepository.save(team);
    }

    /**
     * Обновляет существующую команду
     * @param team - сущность с обновленными данными команды
     * @return обновленная сущность Team
     */
    @Override
    public Team update(Team team) {
        LOG.debug("Запрос на обновление команды: {}", team);
        return teamRepository.save(team);
    }

    /**
     * Частично обновляет данные команды
     * @param team - сущность с полями для обновления
     * @return Optional с обновленной командой, если команда найдена
     */
    @Override
    public Optional<Team> partialUpdate(Team team) {
        LOG.debug("Запрос на частичное обновление команды: {}", team);

        return teamRepository
            .findById(team.getId())
            .map(existingTeam -> {
                // Обновляем только название команды, если оно указано
                if (team.getTeamName() != null) {
                    existingTeam.setTeamName(team.getTeamName());
                }

                return existingTeam;
            })
            .map(teamRepository::save);
    }

    /**
     * Получает список всех команд с поддержкой пагинации
     * @param pageable - параметры пагинации (номер страницы, размер страницы)
     * @return страница с командами
     */
    @Override
    @Transactional(readOnly = true) // Оптимизация для операций чтения
    public Page<Team> findAll(Pageable pageable) {
        LOG.debug("Запрос на получение всех команд");
        return teamRepository.findAll(pageable);
    }

    /**
     * Находит команду по уникальному идентификатору
     * @param id - идентификатор команды
     * @return Optional с найденной командой или пустой
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Team> findOne(Long id) {
        LOG.debug("Запрос на получение команды по ID: {}", id);
        return teamRepository.findById(id);
    }

    /**
     * Удаляет команду по идентификатору
     * @param id - идентификатор команды для удаления
     */
    @Override
    public void delete(Long id) {
        LOG.debug("Запрос на удаление команды с ID: {}", id);
        teamRepository.deleteById(id);
    }

    /**
     * Находит команду по уникальному идентификатору
     * @param id - идентификатор команды СО СПИСКОМ СОТРУДНИКОВ
     * @return Optional с найденной командой или пустой
     */
    @Override
    public Optional<Team> findOneWithEmployees(Long id) {
        return teamRepository.findByIdWithEmployees(id);
    }
}
