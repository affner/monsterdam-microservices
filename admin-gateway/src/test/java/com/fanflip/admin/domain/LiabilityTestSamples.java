package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LiabilityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Liability getLiabilitySample1() {
        return new Liability().id(1L).name("name1");
    }

    public static Liability getLiabilitySample2() {
        return new Liability().id(2L).name("name2");
    }

    public static Liability getLiabilityRandomSampleGenerator() {
        return new Liability().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
