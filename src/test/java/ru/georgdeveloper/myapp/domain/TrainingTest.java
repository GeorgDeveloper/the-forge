package ru.georgdeveloper.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.georgdeveloper.myapp.domain.EmployeeTestSamples.*;
import static ru.georgdeveloper.myapp.domain.TrainingTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.georgdeveloper.myapp.web.rest.TestUtil;

class TrainingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Training.class);
        Training training1 = getTrainingSample1();
        Training training2 = new Training();
        assertThat(training1).isNotEqualTo(training2);

        training2.setId(training1.getId());
        assertThat(training1).isEqualTo(training2);

        training2 = getTrainingSample2();
        assertThat(training1).isNotEqualTo(training2);
    }

    @Test
    void employeeTest() {
        Training training = getTrainingRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        training.setEmployee(employeeBack);
        assertThat(training.getEmployee()).isEqualTo(employeeBack);

        training.employee(null);
        assertThat(training.getEmployee()).isNull();
    }
}
