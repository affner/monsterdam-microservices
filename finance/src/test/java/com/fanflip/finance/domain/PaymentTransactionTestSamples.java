package com.fanflip.finance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentTransaction getPaymentTransactionSample1() {
        return new PaymentTransaction()
            .id(1L)
            .paymentReference("paymentReference1")
            .cloudTransactionId("cloudTransactionId1")
            .viewerId(1L)
            .paymentMethodId(1L)
            .paymentProviderId(1L);
    }

    public static PaymentTransaction getPaymentTransactionSample2() {
        return new PaymentTransaction()
            .id(2L)
            .paymentReference("paymentReference2")
            .cloudTransactionId("cloudTransactionId2")
            .viewerId(2L)
            .paymentMethodId(2L)
            .paymentProviderId(2L);
    }

    public static PaymentTransaction getPaymentTransactionRandomSampleGenerator() {
        return new PaymentTransaction()
            .id(longCount.incrementAndGet())
            .paymentReference(UUID.randomUUID().toString())
            .cloudTransactionId(UUID.randomUUID().toString())
            .viewerId(longCount.incrementAndGet())
            .paymentMethodId(longCount.incrementAndGet())
            .paymentProviderId(longCount.incrementAndGet());
    }
}
