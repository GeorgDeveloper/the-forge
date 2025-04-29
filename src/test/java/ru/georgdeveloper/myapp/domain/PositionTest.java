package ru.georgdeveloper.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.georgdeveloper.myapp.domain.EmployeeTestSamples.*;
import static ru.georgdeveloper.myapp.domain.JobDescriptionTestSamples.*;
import static ru.georgdeveloper.myapp.domain.PositionTestSamples.*;
import static ru.georgdeveloper.myapp.domain.SafetyInstructionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.georgdeveloper.myapp.web.rest.TestUtil;

class PositionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Position.class);
        Position position1 = getPositionSample1();
        Position position2 = new Position();
        assertThat(position1).isNotEqualTo(position2);

        position2.setId(position1.getId());
        assertThat(position1).isEqualTo(position2);

        position2 = getPositionSample2();
        assertThat(position1).isNotEqualTo(position2);
    }

    @Test
    void jobDescriptionTest() {
        Position position = getPositionRandomSampleGenerator();
        JobDescription jobDescriptionBack = getJobDescriptionRandomSampleGenerator();

        position.setJobDescription(jobDescriptionBack);
        assertThat(position.getJobDescription()).isEqualTo(jobDescriptionBack);

        position.jobDescription(null);
        assertThat(position.getJobDescription()).isNull();
    }

    @Test
    void safetyInstructionTest() {
        Position position = getPositionRandomSampleGenerator();
        SafetyInstruction safetyInstructionBack = getSafetyInstructionRandomSampleGenerator();

        position.addSafetyInstruction(safetyInstructionBack);
        assertThat(position.getSafetyInstructions()).containsOnly(safetyInstructionBack);
        assertThat(safetyInstructionBack.getPosition()).isEqualTo(position);

        position.removeSafetyInstruction(safetyInstructionBack);
        assertThat(position.getSafetyInstructions()).doesNotContain(safetyInstructionBack);
        assertThat(safetyInstructionBack.getPosition()).isNull();

        position.setSafetyInstructions(new HashSet<>(Set.of(safetyInstructionBack)));
        assertThat(position.getSafetyInstructions()).containsOnly(safetyInstructionBack);
        assertThat(safetyInstructionBack.getPosition()).isEqualTo(position);

        position.setSafetyInstructions(new HashSet<>());
        assertThat(position.getSafetyInstructions()).doesNotContain(safetyInstructionBack);
        assertThat(safetyInstructionBack.getPosition()).isNull();
    }

    @Test
    void employeeTest() {
        Position position = getPositionRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        position.addEmployee(employeeBack);
        assertThat(position.getEmployees()).containsOnly(employeeBack);
        assertThat(employeeBack.getPosition()).isEqualTo(position);

        position.removeEmployee(employeeBack);
        assertThat(position.getEmployees()).doesNotContain(employeeBack);
        assertThat(employeeBack.getPosition()).isNull();

        position.employees(new HashSet<>(Set.of(employeeBack)));
        assertThat(position.getEmployees()).containsOnly(employeeBack);
        assertThat(employeeBack.getPosition()).isEqualTo(position);

        position.setEmployees(new HashSet<>());
        assertThat(position.getEmployees()).doesNotContain(employeeBack);
        assertThat(employeeBack.getPosition()).isNull();
    }
}
