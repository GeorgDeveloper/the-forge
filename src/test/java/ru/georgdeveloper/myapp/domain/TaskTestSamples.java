package ru.georgdeveloper.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TaskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Task getTaskSample1() {
        return new Task().id(1L).taskName("taskName1").body("body1");
    }

    public static Task getTaskSample2() {
        return new Task().id(2L).taskName("taskName2").body("body2");
    }

    public static Task getTaskRandomSampleGenerator() {
        return new Task().id(longCount.incrementAndGet()).taskName(UUID.randomUUID().toString()).body(UUID.randomUUID().toString());
    }
}
