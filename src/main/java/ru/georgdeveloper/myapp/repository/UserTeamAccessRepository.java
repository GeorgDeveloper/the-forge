package ru.georgdeveloper.myapp.repository;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.georgdeveloper.myapp.domain.Team;
import ru.georgdeveloper.myapp.domain.User;
import ru.georgdeveloper.myapp.domain.UserTeamAccess;
import ru.georgdeveloper.myapp.domain.enumeration.AccessLevel;

public interface UserTeamAccessRepository extends JpaRepository<UserTeamAccess, Long> {
    boolean existsByUserAndTeam(User user, Team team);

    boolean existsByUserAndTeamAndAccessLevel(User user, Team team, AccessLevel accessLevel);

    void deleteByTeam_Id(Long teamId);

    @Query("SELECT u FROM UserTeamAccess u WHERE u.team = :team")
    Set<UserTeamAccess> findByTeam(@Param("team") Team team);

    Optional<UserTeamAccess> findByTeamIdAndUserId(Long teamId, Long userId);
}
