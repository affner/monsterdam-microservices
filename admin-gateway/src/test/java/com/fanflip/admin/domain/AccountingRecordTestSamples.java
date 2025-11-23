package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AccountingRecordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AccountingRecord getAccountingRecordSample1() {
        return new AccountingRecord().id(1L).description("description1").paymentId(1L);
    }

    public static AccountingRecord getAccountingRecordSample2() {
        return new AccountingRecord().id(2L).description("description2").paymentId(2L);
    }

    public static AccountingRecord getAccountingRecordRandomSampleGenerator() {
        return new AccountingRecord()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .paymentId(longCount.incrementAndGet());
    }
}
