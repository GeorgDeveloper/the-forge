package ru.georgdeveloper.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AdditionalTrainingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AdditionalTraining getAdditionalTrainingSample1() {
        return new AdditionalTraining().id(1L).trainingName("trainingName1").validityPeriod(1);
    }

    public static AdditionalTraining getAdditionalTrainingSample2() {
        return new AdditionalTraining().id(2L).trainingName("trainingName2").validityPeriod(2);
    }

    public static AdditionalTraining getAdditionalTrainingRandomSampleGenerator() {
        return new AdditionalTraining()
            .id(longCount.incrementAndGet())
            .trainingName(UUID.randomUUID().toString())
            .validityPeriod(intCount.incrementAndGet());
    }
}
