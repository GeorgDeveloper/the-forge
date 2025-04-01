package ru.georgdeveloper.myapp.service;

import java.security.Principal;
import ru.georgdeveloper.myapp.domain.Team;
import ru.georgdeveloper.myapp.domain.User;

public interface TeamAccessService {
    // Создать команду (автоматически дает доступ владельца)
    Team createTeam(Long userID, Team team);

    // Удалить команду
    void deleteTeam(Long teamID);

    // Предоставить доступ другому пользователю
    void grantAccess(Team team, User owner, User userToShareWith);

    // Проверить, есть ли у пользователя доступ к команде
    boolean hasAccess(User user, Team team);

    // Проверить, является ли пользователь владельцем команды
    boolean hasOwnerAccess(User user, Team team);
}
