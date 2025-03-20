package ru.georgdeveloper.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SafetyInstructionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SafetyInstruction getSafetyInstructionSample1() {
        return new SafetyInstruction().id(1L).instructionName("instructionName1");
    }

    public static SafetyInstruction getSafetyInstructionSample2() {
        return new SafetyInstruction().id(2L).instructionName("instructionName2");
    }

    public static SafetyInstruction getSafetyInstructionRandomSampleGenerator() {
        return new SafetyInstruction().id(longCount.incrementAndGet()).instructionName(UUID.randomUUID().toString());
    }
}
