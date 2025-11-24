package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PurchasedSubscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PurchasedSubscription getPurchasedSubscriptionSample1() {
        return new PurchasedSubscription().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1").viewerId(1L).creatorId(1L);
    }

    public static PurchasedSubscription getPurchasedSubscriptionSample2() {
        return new PurchasedSubscription().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2").viewerId(2L).creatorId(2L);
    }

    public static PurchasedSubscription getPurchasedSubscriptionRandomSampleGenerator() {
        return new PurchasedSubscription()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .viewerId(longCount.incrementAndGet())
            .creatorId(longCount.incrementAndGet());
    }
}
