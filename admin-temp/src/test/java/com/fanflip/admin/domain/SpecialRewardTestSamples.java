package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SpecialRewardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SpecialReward getSpecialRewardSample1() {
        return new SpecialReward()
            .id(1L)
            .description("description1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .contentPackageId(1L)
            .viewerId(1L)
            .offerPromotionId(1L);
    }

    public static SpecialReward getSpecialRewardSample2() {
        return new SpecialReward()
            .id(2L)
            .description("description2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .contentPackageId(2L)
            .viewerId(2L)
            .offerPromotionId(2L);
    }

    public static SpecialReward getSpecialRewardRandomSampleGenerator() {
        return new SpecialReward()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .contentPackageId(longCount.incrementAndGet())
            .viewerId(longCount.incrementAndGet())
            .offerPromotionId(longCount.incrementAndGet());
    }
}
