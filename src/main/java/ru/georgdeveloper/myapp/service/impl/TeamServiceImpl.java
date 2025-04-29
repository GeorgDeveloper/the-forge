package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Employee;
import ru.georgdeveloper.myapp.domain.Team;
import ru.georgdeveloper.myapp.domain.User;
import ru.georgdeveloper.myapp.domain.UserTeamAccess;
import ru.georgdeveloper.myapp.domain.enumeration.AccessLevel;
import ru.georgdeveloper.myapp.repository.EmployeeRepository;
import ru.georgdeveloper.myapp.repository.TeamRepository;
import ru.georgdeveloper.myapp.repository.UserTeamAccessRepository;
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
    private final UserTeamAccessRepository userTeamAccessRepository;
    private final EmployeeRepository employeeRepository;
    private final TeamAccessServiceImpl teamAccessService;

    /**
     * Конструктор с внедрением зависимости TeamRepository
     *
     * @param teamRepository           - репозиторий для работы с командами
     * @param userTeamAccessRepository
     * @param teamAccessService
     */
    public TeamServiceImpl(
        TeamRepository teamRepository,
        UserTeamAccessRepository userTeamAccessRepository,
        EmployeeRepository employeeRepository,
        TeamAccessServiceImpl teamAccessService
    ) {
        this.teamRepository = teamRepository;
        this.userTeamAccessRepository = userTeamAccessRepository;
        this.employeeRepository = employeeRepository;
        this.teamAccessService = teamAccessService;
    }

    /**
     * Сохраняет новую команду в системе
     * @param team - сущность команды для сохранения
     * @return сохраненная сущность Team
     */
    @Override
    public Team save(Team team) {
        LOG.debug("Запрос на сохранение команды: {}", team);

        // Сначала сохраняем саму команду (без сотрудников)
        Team savedTeam = teamRepository.save(team);

        // Затем обновляем связи с сотрудниками
        if (team.getEmployees() != null) {
            Set<Employee> employees = (Set<Employee>) team.getEmployees();
            for (Employee employee : employees) {
                Optional<Employee> employeeOpt = employeeRepository.findById(employee.getId());
                if (employeeOpt.isPresent()) {
                    Employee employeeToUpdate = employeeOpt.get();
                    employeeToUpdate.setTeam(savedTeam);
                    employeeRepository.save(employeeToUpdate);
                }
            }
        }

        return savedTeam;
    }

    /**
     * Обновляет существующую команду
     * @param team - сущность с обновленными данными команды
     * @return обновленная сущность Team
     */
    @Override
    @Transactional
    public Team update(Team team) {
        LOG.debug("Запрос на обновление команды: {}", team);

        // 1. Сохраняем основную информацию о команде
        Team savedTeam = teamRepository.save(team);

        // 2. Обработка сотрудников
        if (team.getEmployees() != null) {
            updateTeamEmployees(savedTeam, team.getEmployees());
        }

        // 3. Обработка пользователей (новый функционал)
        if (team.getUsers() != null) {
            teamAccessService.updateTeamUsers(savedTeam, team.getUsers());
        }

        return savedTeam;
    }

    private void updateTeamEmployees(Team team, Set<Employee> employees) {
        // Существующая логика для сотрудников
        Set<Employee> currentEmployees = team.getEmployees();

        // Удаляем сотрудников, которых больше нет в команде
        currentEmployees.removeIf(emp -> !employees.contains(emp));

        // Добавляем новых сотрудников
        for (Employee employee : employees) {
            if (!currentEmployees.contains(employee)) {
                employee.setTeam(team);
                employeeRepository.save(employee);
            }
        }
    }

    //    @Override
    //    public Team update(Team team) {
    //        LOG.debug("Запрос на обновление команды: {}", team);
    //        Team savedTeam = teamRepository.save(team);
    //
    //        // Затем обновляем связи с сотрудниками
    //        if (team.getEmployees() != null) {
    //            Set<Employee> employees = (Set<Employee>) team.getEmployees();
    //            for (Employee employee : employees) {
    //                Optional<Employee> employeeOpt = employeeRepository.findById(employee.getId());
    //                if (employeeOpt.isPresent()) {
    //                    Employee employeeToUpdate = employeeOpt.get();
    //                    employeeToUpdate.setTeam(savedTeam);
    //                    employeeRepository.save(employeeToUpdate);
    //                }
    //            }
    //        }
    //        return savedTeam;
    //    }

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
                if (team.getEmployees() != null) {
                    Set<Employee> employees = (Set<Employee>) team.getEmployees();
                    for (Employee employee : employees) {
                        Optional<Employee> employeeOpt = employeeRepository.findById(employee.getId());
                        if (employeeOpt.isPresent()) {
                            Employee employeeToUpdate = employeeOpt.get();
                            employeeToUpdate.setTeam(existingTeam);
                            employeeRepository.save(employeeToUpdate);
                        }
                    }
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
     * Получает список всех команд по ID пользователя с поддержкой пагинации
     * @param pageable - параметры пагинации (номер страницы, размер страницы)
     * @return страница с командами
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Team> findAllByIdUser(Pageable pageable, Long idUser) {
        LOG.debug("Запрос на получение всех команд по ID user: {}", idUser);
        return teamRepository.findAllByUserId(pageable, idUser);
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
        // Затем обновляем связи с сотрудниками
        Set<Employee> employees = (Set<Employee>) teamRepository.findById(id).get().getEmployees();
        for (Employee employee : employees) {
            Optional<Employee> employeeOpt = employeeRepository.findById(employee.getId());
            if (employeeOpt.isPresent()) {
                Employee employeeToUpdate = employeeOpt.get();
                employeeToUpdate.setTeam(null);
                employeeRepository.save(employeeToUpdate);
            }
        }
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
