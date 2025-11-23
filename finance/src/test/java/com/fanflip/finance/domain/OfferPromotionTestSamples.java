package com.fanflip.finance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OfferPromotionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OfferPromotion getOfferPromotionSample1() {
        return new OfferPromotion()
            .id(1L)
            .subscriptionsLimit(1)
            .linkCode("linkCode1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static OfferPromotion getOfferPromotionSample2() {
        return new OfferPromotion()
            .id(2L)
            .subscriptionsLimit(2)
            .linkCode("linkCode2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static OfferPromotion getOfferPromotionRandomSampleGenerator() {
        return new OfferPromotion()
            .id(longCount.incrementAndGet())
            .subscriptionsLimit(intCount.incrementAndGet())
            .linkCode(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
