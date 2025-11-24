package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CreatorEarningTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CreatorEarning getCreatorEarningSample1() {
        return new CreatorEarning().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static CreatorEarning getCreatorEarningSample2() {
        return new CreatorEarning().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static CreatorEarning getCreatorEarningRandomSampleGenerator() {
        return new CreatorEarning()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
