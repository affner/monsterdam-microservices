package com.monsterdam.finance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PurchasedTipTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PurchasedTip getPurchasedTipSample1() {
        return new PurchasedTip().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1").messageId(1L);
    }

    public static PurchasedTip getPurchasedTipSample2() {
        return new PurchasedTip().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2").messageId(2L);
    }

    public static PurchasedTip getPurchasedTipRandomSampleGenerator() {
        return new PurchasedTip()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .messageId(longCount.incrementAndGet());
    }
}
