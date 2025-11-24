package com.monsterdam.finance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WalletTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WalletTransaction getWalletTransactionSample1() {
        return new WalletTransaction().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1").viewerId(1L);
    }

    public static WalletTransaction getWalletTransactionSample2() {
        return new WalletTransaction().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2").viewerId(2L);
    }

    public static WalletTransaction getWalletTransactionRandomSampleGenerator() {
        return new WalletTransaction()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .viewerId(longCount.incrementAndGet());
    }
}
