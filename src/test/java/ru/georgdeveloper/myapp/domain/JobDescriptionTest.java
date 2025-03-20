package ru.georgdeveloper.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.georgdeveloper.myapp.domain.JobDescriptionTestSamples.*;
import static ru.georgdeveloper.myapp.domain.PositionTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.georgdeveloper.myapp.web.rest.TestUtil;

class JobDescriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobDescription.class);
        JobDescription jobDescription1 = getJobDescriptionSample1();
        JobDescription jobDescription2 = new JobDescription();
        assertThat(jobDescription1).isNotEqualTo(jobDescription2);

        jobDescription2.setId(jobDescription1.getId());
        assertThat(jobDescription1).isEqualTo(jobDescription2);

        jobDescription2 = getJobDescriptionSample2();
        assertThat(jobDescription1).isNotEqualTo(jobDescription2);
    }

    @Test
    void positionTest() {
        JobDescription jobDescription = getJobDescriptionRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        jobDescription.setPosition(positionBack);
        assertThat(jobDescription.getPosition()).isEqualTo(positionBack);
        assertThat(positionBack.getJobDescription()).isEqualTo(jobDescription);

        jobDescription.position(null);
        assertThat(jobDescription.getPosition()).isNull();
        assertThat(positionBack.getJobDescription()).isNull();
    }
}
