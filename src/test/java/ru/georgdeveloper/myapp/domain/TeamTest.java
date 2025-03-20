package ru.georgdeveloper.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.georgdeveloper.myapp.domain.EmployeeTestSamples.*;
import static ru.georgdeveloper.myapp.domain.TeamTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.georgdeveloper.myapp.web.rest.TestUtil;

class TeamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Team.class);
        Team team1 = getTeamSample1();
        Team team2 = new Team();
        assertThat(team1).isNotEqualTo(team2);

        team2.setId(team1.getId());
        assertThat(team1).isEqualTo(team2);

        team2 = getTeamSample2();
        assertThat(team1).isNotEqualTo(team2);
    }

    @Test
    void employeeTest() {
        Team team = getTeamRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        team.addEmployee(employeeBack);
        assertThat(team.getEmployees()).containsOnly(employeeBack);
        assertThat(employeeBack.getTeam()).isEqualTo(team);

        team.removeEmployee(employeeBack);
        assertThat(team.getEmployees()).doesNotContain(employeeBack);
        assertThat(employeeBack.getTeam()).isNull();

        team.employees(new HashSet<>(Set.of(employeeBack)));
        assertThat(team.getEmployees()).containsOnly(employeeBack);
        assertThat(employeeBack.getTeam()).isEqualTo(team);

        team.setEmployees(new HashSet<>());
        assertThat(team.getEmployees()).doesNotContain(employeeBack);
        assertThat(employeeBack.getTeam()).isNull();
    }
}
