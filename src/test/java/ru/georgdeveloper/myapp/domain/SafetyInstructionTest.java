package ru.georgdeveloper.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.georgdeveloper.myapp.domain.PositionTestSamples.*;
import static ru.georgdeveloper.myapp.domain.ProfessionTestSamples.*;
import static ru.georgdeveloper.myapp.domain.SafetyInstructionTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.georgdeveloper.myapp.web.rest.TestUtil;

class SafetyInstructionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SafetyInstruction.class);
        SafetyInstruction safetyInstruction1 = getSafetyInstructionSample1();
        SafetyInstruction safetyInstruction2 = new SafetyInstruction();
        assertThat(safetyInstruction1).isNotEqualTo(safetyInstruction2);

        safetyInstruction2.setId(safetyInstruction1.getId());
        assertThat(safetyInstruction1).isEqualTo(safetyInstruction2);

        safetyInstruction2 = getSafetyInstructionSample2();
        assertThat(safetyInstruction1).isNotEqualTo(safetyInstruction2);
    }

    @Test
    void professionTest() {
        SafetyInstruction safetyInstruction = getSafetyInstructionRandomSampleGenerator();
        Profession professionBack = getProfessionRandomSampleGenerator();

        safetyInstruction.setProfession(professionBack);
        assertThat(safetyInstruction.getProfession()).isEqualTo(professionBack);

        safetyInstruction.profession(null);
        assertThat(safetyInstruction.getProfession()).isNull();
    }

    @Test
    void positionTest() {
        SafetyInstruction safetyInstruction = getSafetyInstructionRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        safetyInstruction.setPosition(positionBack);
        assertThat(safetyInstruction.getPosition()).isEqualTo(positionBack);

        safetyInstruction.position(null);
        assertThat(safetyInstruction.getPosition()).isNull();
    }
}
