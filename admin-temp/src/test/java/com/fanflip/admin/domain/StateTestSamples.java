package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static State getStateSample1() {
        return new State().id(1L).stateName("stateName1").isoCode("isoCode1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static State getStateSample2() {
        return new State().id(2L).stateName("stateName2").isoCode("isoCode2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static State getStateRandomSampleGenerator() {
        return new State()
            .id(longCount.incrementAndGet())
            .stateName(UUID.randomUUID().toString())
            .isoCode(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
