package ru.georgdeveloper.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.georgdeveloper.myapp.domain.AdditionalTrainingTestSamples.*;
import static ru.georgdeveloper.myapp.domain.ProfessionTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.georgdeveloper.myapp.web.rest.TestUtil;

class AdditionalTrainingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdditionalTraining.class);
        AdditionalTraining additionalTraining1 = getAdditionalTrainingSample1();
        AdditionalTraining additionalTraining2 = new AdditionalTraining();
        assertThat(additionalTraining1).isNotEqualTo(additionalTraining2);

        additionalTraining2.setId(additionalTraining1.getId());
        assertThat(additionalTraining1).isEqualTo(additionalTraining2);

        additionalTraining2 = getAdditionalTrainingSample2();
        assertThat(additionalTraining1).isNotEqualTo(additionalTraining2);
    }

    @Test
    void professionTest() {
        AdditionalTraining additionalTraining = getAdditionalTrainingRandomSampleGenerator();
        Profession professionBack = getProfessionRandomSampleGenerator();

        additionalTraining.setProfession(professionBack);
        assertThat(additionalTraining.getProfession()).isEqualTo(professionBack);

        additionalTraining.profession(null);
        assertThat(additionalTraining.getProfession()).isNull();
    }
}
