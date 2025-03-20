package ru.georgdeveloper.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrainingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Training getTrainingSample1() {
        return new Training().id(1L).trainingName("trainingName1").validityPeriod(1);
    }

    public static Training getTrainingSample2() {
        return new Training().id(2L).trainingName("trainingName2").validityPeriod(2);
    }

    public static Training getTrainingRandomSampleGenerator() {
        return new Training()
            .id(longCount.incrementAndGet())
            .trainingName(UUID.randomUUID().toString())
            .validityPeriod(intCount.incrementAndGet());
    }
}
