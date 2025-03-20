package ru.georgdeveloper.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class JobDescriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static JobDescription getJobDescriptionSample1() {
        return new JobDescription().id(1L).descriptionName("descriptionName1");
    }

    public static JobDescription getJobDescriptionSample2() {
        return new JobDescription().id(2L).descriptionName("descriptionName2");
    }

    public static JobDescription getJobDescriptionRandomSampleGenerator() {
        return new JobDescription().id(longCount.incrementAndGet()).descriptionName(UUID.randomUUID().toString());
    }
}
