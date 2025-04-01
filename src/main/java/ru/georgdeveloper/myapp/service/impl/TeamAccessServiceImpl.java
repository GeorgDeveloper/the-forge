package ru.georgdeveloper.myapp.service.impl;

import java.security.Principal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Team;
import ru.georgdeveloper.myapp.domain.User;
import ru.georgdeveloper.myapp.domain.UserTeamAccess;
import ru.georgdeveloper.myapp.domain.enumeration.AccessLevel;
import ru.georgdeveloper.myapp.repository.UserRepository;
import ru.georgdeveloper.myapp.repository.UserTeamAccessRepository;
import ru.georgdeveloper.myapp.service.TeamAccessService;

@Service
@Transactional
public class TeamAccessServiceImpl implements TeamAccessService {

    private final UserRepository userRepository;
    private final UserTeamAccessRepository accessRepository;
    private final TeamServiceImpl teamServiceImpl;

    public TeamAccessServiceImpl(
        UserRepository userRepository,
        UserTeamAccessRepository accessRepository,
        TeamServiceImpl teamServiceImpl
    ) {
        this.userRepository = userRepository;
        this.accessRepository = accessRepository;
        this.teamServiceImpl = teamServiceImpl;
    }

    // Создать команду (автоматически дает доступ владельца)
    @Override
    public Team createTeam(Long userID, Team team) {
        // Сначала сохраняем команду (без связей)
        Team savedTeam = teamServiceImpl.save(team);

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
    public void deleteTeam(Long teamID) {
        accessRepository.deleteByTeam_Id(teamID);
        teamServiceImpl.delete(teamID);
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
