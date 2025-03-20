package ru.georgdeveloper.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.georgdeveloper.myapp.domain.EmployeeTestSamples.*;
import static ru.georgdeveloper.myapp.domain.PositionTestSamples.*;
import static ru.georgdeveloper.myapp.domain.ProfessionTestSamples.*;
import static ru.georgdeveloper.myapp.domain.TaskTestSamples.*;
import static ru.georgdeveloper.myapp.domain.TeamTestSamples.*;
import static ru.georgdeveloper.myapp.domain.TrainingTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.georgdeveloper.myapp.web.rest.TestUtil;

class EmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = getEmployeeSample1();
        Employee employee2 = new Employee();
        assertThat(employee1).isNotEqualTo(employee2);

        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);

        employee2 = getEmployeeSample2();
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    void trainingTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Training trainingBack = getTrainingRandomSampleGenerator();

        employee.addTraining(trainingBack);
        assertThat(employee.getTrainings()).containsOnly(trainingBack);
        assertThat(trainingBack.getEmployee()).isEqualTo(employee);

        employee.removeTraining(trainingBack);
        assertThat(employee.getTrainings()).doesNotContain(trainingBack);
        assertThat(trainingBack.getEmployee()).isNull();

        employee.trainings(new HashSet<>(Set.of(trainingBack)));
        assertThat(employee.getTrainings()).containsOnly(trainingBack);
        assertThat(trainingBack.getEmployee()).isEqualTo(employee);

        employee.setTrainings(new HashSet<>());
        assertThat(employee.getTrainings()).doesNotContain(trainingBack);
        assertThat(trainingBack.getEmployee()).isNull();
    }

    @Test
    void taskTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        employee.addTask(taskBack);
        assertThat(employee.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getEmployee()).isEqualTo(employee);

        employee.removeTask(taskBack);
        assertThat(employee.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getEmployee()).isNull();

        employee.tasks(new HashSet<>(Set.of(taskBack)));
        assertThat(employee.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getEmployee()).isEqualTo(employee);

        employee.setTasks(new HashSet<>());
        assertThat(employee.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getEmployee()).isNull();
    }

    @Test
    void positionTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        employee.setPosition(positionBack);
        assertThat(employee.getPosition()).isEqualTo(positionBack);

        employee.position(null);
        assertThat(employee.getPosition()).isNull();
    }

    @Test
    void professionTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Profession professionBack = getProfessionRandomSampleGenerator();

        employee.addProfession(professionBack);
        assertThat(employee.getProfessions()).containsOnly(professionBack);

        employee.removeProfession(professionBack);
        assertThat(employee.getProfessions()).doesNotContain(professionBack);

        employee.professions(new HashSet<>(Set.of(professionBack)));
        assertThat(employee.getProfessions()).containsOnly(professionBack);

        employee.setProfessions(new HashSet<>());
        assertThat(employee.getProfessions()).doesNotContain(professionBack);
    }

    @Test
    void teamTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Team teamBack = getTeamRandomSampleGenerator();

        employee.setTeam(teamBack);
        assertThat(employee.getTeam()).isEqualTo(teamBack);

        employee.team(null);
        assertThat(employee.getTeam()).isNull();
    }
}
