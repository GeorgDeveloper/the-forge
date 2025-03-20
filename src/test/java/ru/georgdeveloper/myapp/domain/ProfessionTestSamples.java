package ru.georgdeveloper.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Profession getProfessionSample1() {
        return new Profession().id(1L).professionName("professionName1");
    }

    public static Profession getProfessionSample2() {
        return new Profession().id(2L).professionName("professionName2");
    }

    public static Profession getProfessionRandomSampleGenerator() {
        return new Profession().id(longCount.incrementAndGet()).professionName(UUID.randomUUID().toString());
    }
}
