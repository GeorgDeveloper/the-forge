package ru.georgdeveloper.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProtectiveEquipmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProtectiveEquipment getProtectiveEquipmentSample1() {
        return new ProtectiveEquipment().id(1L).equipmentName("equipmentName1").quantity(1).issuanceFrequency(1);
    }

    public static ProtectiveEquipment getProtectiveEquipmentSample2() {
        return new ProtectiveEquipment().id(2L).equipmentName("equipmentName2").quantity(2).issuanceFrequency(2);
    }

    public static ProtectiveEquipment getProtectiveEquipmentRandomSampleGenerator() {
        return new ProtectiveEquipment()
            .id(longCount.incrementAndGet())
            .equipmentName(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet())
            .issuanceFrequency(intCount.incrementAndGet());
    }
}
