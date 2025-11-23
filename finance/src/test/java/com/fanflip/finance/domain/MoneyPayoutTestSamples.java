package com.fanflip.finance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MoneyPayoutTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MoneyPayout getMoneyPayoutSample1() {
        return new MoneyPayout().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1").creatorId(1L);
    }

    public static MoneyPayout getMoneyPayoutSample2() {
        return new MoneyPayout().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2").creatorId(2L);
    }

    public static MoneyPayout getMoneyPayoutRandomSampleGenerator() {
        return new MoneyPayout()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .creatorId(longCount.incrementAndGet());
    }
}
