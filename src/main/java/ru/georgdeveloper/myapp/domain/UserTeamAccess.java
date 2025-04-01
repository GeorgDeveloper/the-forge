package ru.georgdeveloper.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import ru.georgdeveloper.myapp.domain.enumeration.AccessLevel;

@Entity
@Table(name = "user_team_access")
public class UserTeamAccess implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_level", nullable = false)
    private AccessLevel accessLevel; // OWNER, VIEWER и т.д.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserTeamAccess that = (UserTeamAccess) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(user, that.user) &&
            Objects.equals(team, that.team) &&
            accessLevel == that.accessLevel
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, team, accessLevel);
    }
}
