package ru.georgdeveloper.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.georgdeveloper.myapp.domain.ProfessionTestSamples.*;
import static ru.georgdeveloper.myapp.domain.ProtectiveEquipmentTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.georgdeveloper.myapp.web.rest.TestUtil;

class ProtectiveEquipmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProtectiveEquipment.class);
        ProtectiveEquipment protectiveEquipment1 = getProtectiveEquipmentSample1();
        ProtectiveEquipment protectiveEquipment2 = new ProtectiveEquipment();
        assertThat(protectiveEquipment1).isNotEqualTo(protectiveEquipment2);

        protectiveEquipment2.setId(protectiveEquipment1.getId());
        assertThat(protectiveEquipment1).isEqualTo(protectiveEquipment2);

        protectiveEquipment2 = getProtectiveEquipmentSample2();
        assertThat(protectiveEquipment1).isNotEqualTo(protectiveEquipment2);
    }

    @Test
    void professionTest() {
        ProtectiveEquipment protectiveEquipment = getProtectiveEquipmentRandomSampleGenerator();
        Profession professionBack = getProfessionRandomSampleGenerator();

        protectiveEquipment.setProfession(professionBack);
        assertThat(protectiveEquipment.getProfession()).isEqualTo(professionBack);

        protectiveEquipment.profession(null);
        assertThat(protectiveEquipment.getProfession()).isNull();
    }
}
