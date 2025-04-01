package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.georgdeveloper.myapp.domain.Team;
import ru.georgdeveloper.myapp.domain.User;
import ru.georgdeveloper.myapp.domain.UserTeamAccess;
import ru.georgdeveloper.myapp.domain.enumeration.AccessLevel;

public interface UserTeamAccessRepository extends JpaRepository<UserTeamAccess, Long> {
    boolean existsByUserAndTeam(User user, Team team);

    boolean existsByUserAndTeamAndAccessLevel(User user, Team team, AccessLevel accessLevel);
}
