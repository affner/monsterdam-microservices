package com.fanflip.finance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SubscriptionBundleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SubscriptionBundle getSubscriptionBundleSample1() {
        return new SubscriptionBundle().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1").creatorId(1L);
    }

    public static SubscriptionBundle getSubscriptionBundleSample2() {
        return new SubscriptionBundle().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2").creatorId(2L);
    }

    public static SubscriptionBundle getSubscriptionBundleRandomSampleGenerator() {
        return new SubscriptionBundle()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .creatorId(longCount.incrementAndGet());
    }
}
