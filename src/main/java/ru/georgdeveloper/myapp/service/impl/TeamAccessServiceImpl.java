package ru.georgdeveloper.myapp.service.impl;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Team;
import ru.georgdeveloper.myapp.domain.User;
import ru.georgdeveloper.myapp.domain.UserTeamAccess;
import ru.georgdeveloper.myapp.domain.enumeration.AccessLevel;
import ru.georgdeveloper.myapp.repository.TeamRepository;
import ru.georgdeveloper.myapp.repository.UserRepository;
import ru.georgdeveloper.myapp.repository.UserTeamAccessRepository;
import ru.georgdeveloper.myapp.service.TeamAccessService;

@Service
@Transactional
public class TeamAccessServiceImpl implements TeamAccessService {

    private final UserRepository userRepository;
    private final UserTeamAccessRepository accessRepository;

    private final TeamRepository teamRepository;

    public TeamAccessServiceImpl(UserRepository userRepository, UserTeamAccessRepository accessRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.accessRepository = accessRepository;
        this.teamRepository = teamRepository;
    }

    // Создать команду (автоматически дает доступ владельца)
    @Override
    public Team createTeam(Long userID, Team team) {
        // Сначала сохраняем команду (без связей)
        Team savedTeam = teamRepository.save(team);

        User owner = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));

        // Создаем новый доступ (не устанавливаем ID вручную)
        UserTeamAccess access = new UserTeamAccess();
        access.setUser(owner);
        access.setTeam(savedTeam);
        access.setAccessLevel(AccessLevel.OWNER);

        // Сохраняем доступ через репозиторий
        accessRepository.save(access);

        return savedTeam;
    }

    @Override
    @Transactional
    public void updateTeamUsers(Team team, Set<User> users) {
        // Удаляем старые связи, которые больше не актуальны
        Set<UserTeamAccess> existingAccesses = accessRepository.findByTeam(team);
        Set<Long> existingUserIds = existingAccesses.stream().map(access -> access.getUser().getId()).collect(Collectors.toSet());

        Set<Long> newUserIds = users.stream().map(User::getId).collect(Collectors.toSet());

        // Удаляем доступы для пользователей, которых больше нет в команде
        for (UserTeamAccess access : existingAccesses) {
            if (!newUserIds.contains(access.getUser().getId())) {
                accessRepository.delete(access);
            }
        }

        // Добавляем новых пользователей
        for (User user : users) {
            if (!existingUserIds.contains(user.getId())) {
                UserTeamAccess access = new UserTeamAccess();
                access.setUser(user);
                access.setTeam(team);
                access.setAccessLevel(AccessLevel.VIEWER); // Или другой уровень доступа по умолчанию
                accessRepository.save(access);
            }
        }
    }

    @Override
    public void deleteTeam(Long teamID) {
        accessRepository.deleteByTeam_Id(teamID);
        teamRepository.deleteById(teamID);
    }

    // Предоставить доступ другому пользователю
    @Override
    public void grantAccess(Team team, User owner, User userToShareWith) {
        if (!hasOwnerAccess(owner, team)) {
            throw new SecurityException("Only owner can share access");
        }

        UserTeamAccess access = new UserTeamAccess();
        access.setUser(userToShareWith);
        access.setTeam(team);
        access.setAccessLevel(AccessLevel.VIEWER);

        accessRepository.save(access);
    }

    // Проверить, есть ли у пользователя доступ к команде
    @Override
    public boolean hasAccess(User user, Team team) {
        return accessRepository.existsByUserAndTeam(user, team);
    }

    // Проверить, является ли пользователь владельцем команды
    @Override
    public boolean hasOwnerAccess(User user, Team team) {
        return accessRepository.existsByUserAndTeamAndAccessLevel(user, team, AccessLevel.OWNER);
    }
}
