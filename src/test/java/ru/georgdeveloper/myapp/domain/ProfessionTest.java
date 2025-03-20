package ru.georgdeveloper.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.georgdeveloper.myapp.domain.AdditionalTrainingTestSamples.*;
import static ru.georgdeveloper.myapp.domain.EmployeeTestSamples.*;
import static ru.georgdeveloper.myapp.domain.ProfessionTestSamples.*;
import static ru.georgdeveloper.myapp.domain.ProtectiveEquipmentTestSamples.*;
import static ru.georgdeveloper.myapp.domain.SafetyInstructionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.georgdeveloper.myapp.web.rest.TestUtil;

class ProfessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profession.class);
        Profession profession1 = getProfessionSample1();
        Profession profession2 = new Profession();
        assertThat(profession1).isNotEqualTo(profession2);

        profession2.setId(profession1.getId());
        assertThat(profession1).isEqualTo(profession2);

        profession2 = getProfessionSample2();
        assertThat(profession1).isNotEqualTo(profession2);
    }

    @Test
    void protectiveEquipmentTest() {
        Profession profession = getProfessionRandomSampleGenerator();
        ProtectiveEquipment protectiveEquipmentBack = getProtectiveEquipmentRandomSampleGenerator();

        profession.addProtectiveEquipment(protectiveEquipmentBack);
        assertThat(profession.getProtectiveEquipments()).containsOnly(protectiveEquipmentBack);
        assertThat(protectiveEquipmentBack.getProfession()).isEqualTo(profession);

        profession.removeProtectiveEquipment(protectiveEquipmentBack);
        assertThat(profession.getProtectiveEquipments()).doesNotContain(protectiveEquipmentBack);
        assertThat(protectiveEquipmentBack.getProfession()).isNull();

        profession.protectiveEquipments(new HashSet<>(Set.of(protectiveEquipmentBack)));
        assertThat(profession.getProtectiveEquipments()).containsOnly(protectiveEquipmentBack);
        assertThat(protectiveEquipmentBack.getProfession()).isEqualTo(profession);

        profession.setProtectiveEquipments(new HashSet<>());
        assertThat(profession.getProtectiveEquipments()).doesNotContain(protectiveEquipmentBack);
        assertThat(protectiveEquipmentBack.getProfession()).isNull();
    }

    @Test
    void additionalTrainingTest() {
        Profession profession = getProfessionRandomSampleGenerator();
        AdditionalTraining additionalTrainingBack = getAdditionalTrainingRandomSampleGenerator();

        profession.addAdditionalTraining(additionalTrainingBack);
        assertThat(profession.getAdditionalTrainings()).containsOnly(additionalTrainingBack);
        assertThat(additionalTrainingBack.getProfession()).isEqualTo(profession);

        profession.removeAdditionalTraining(additionalTrainingBack);
        assertThat(profession.getAdditionalTrainings()).doesNotContain(additionalTrainingBack);
        assertThat(additionalTrainingBack.getProfession()).isNull();

        profession.additionalTrainings(new HashSet<>(Set.of(additionalTrainingBack)));
        assertThat(profession.getAdditionalTrainings()).containsOnly(additionalTrainingBack);
        assertThat(additionalTrainingBack.getProfession()).isEqualTo(profession);

        profession.setAdditionalTrainings(new HashSet<>());
        assertThat(profession.getAdditionalTrainings()).doesNotContain(additionalTrainingBack);
        assertThat(additionalTrainingBack.getProfession()).isNull();
    }

    @Test
    void safetyInstructionTest() {
        Profession profession = getProfessionRandomSampleGenerator();
        SafetyInstruction safetyInstructionBack = getSafetyInstructionRandomSampleGenerator();

        profession.addSafetyInstruction(safetyInstructionBack);
        assertThat(profession.getSafetyInstructions()).containsOnly(safetyInstructionBack);
        assertThat(safetyInstructionBack.getProfession()).isEqualTo(profession);

        profession.removeSafetyInstruction(safetyInstructionBack);
        assertThat(profession.getSafetyInstructions()).doesNotContain(safetyInstructionBack);
        assertThat(safetyInstructionBack.getProfession()).isNull();

        profession.safetyInstructions(new HashSet<>(Set.of(safetyInstructionBack)));
        assertThat(profession.getSafetyInstructions()).containsOnly(safetyInstructionBack);
        assertThat(safetyInstructionBack.getProfession()).isEqualTo(profession);

        profession.setSafetyInstructions(new HashSet<>());
        assertThat(profession.getSafetyInstructions()).doesNotContain(safetyInstructionBack);
        assertThat(safetyInstructionBack.getProfession()).isNull();
    }

    @Test
    void employeeTest() {
        Profession profession = getProfessionRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        profession.addEmployee(employeeBack);
        assertThat(profession.getEmployees()).containsOnly(employeeBack);
        assertThat(employeeBack.getProfessions()).containsOnly(profession);

        profession.removeEmployee(employeeBack);
        assertThat(profession.getEmployees()).doesNotContain(employeeBack);
        assertThat(employeeBack.getProfessions()).doesNotContain(profession);

        profession.employees(new HashSet<>(Set.of(employeeBack)));
        assertThat(profession.getEmployees()).containsOnly(employeeBack);
        assertThat(employeeBack.getProfessions()).containsOnly(profession);

        profession.setEmployees(new HashSet<>());
        assertThat(profession.getEmployees()).doesNotContain(employeeBack);
        assertThat(employeeBack.getProfessions()).doesNotContain(profession);
    }
}
