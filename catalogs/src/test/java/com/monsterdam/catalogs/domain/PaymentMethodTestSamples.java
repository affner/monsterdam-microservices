package com.monsterdam.catalogs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentMethodTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentMethod getPaymentMethodSample1() {
        return new PaymentMethod()
            .id(1L)
            .methodName("methodName1")
            .tokenText("tokenText1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static PaymentMethod getPaymentMethodSample2() {
        return new PaymentMethod()
            .id(2L)
            .methodName("methodName2")
            .tokenText("tokenText2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static PaymentMethod getPaymentMethodRandomSampleGenerator() {
        return new PaymentMethod()
            .id(longCount.incrementAndGet())
            .methodName(UUID.randomUUID().toString())
            .tokenText(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
