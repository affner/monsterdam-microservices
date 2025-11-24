package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PurchasedContentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PurchasedContent getPurchasedContentSample1() {
        return new PurchasedContent().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static PurchasedContent getPurchasedContentSample2() {
        return new PurchasedContent().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static PurchasedContent getPurchasedContentRandomSampleGenerator() {
        return new PurchasedContent()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
