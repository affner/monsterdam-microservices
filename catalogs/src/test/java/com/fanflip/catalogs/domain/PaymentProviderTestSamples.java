package com.fanflip.catalogs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentProviderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentProvider getPaymentProviderSample1() {
        return new PaymentProvider()
            .id(1L)
            .providerName("providerName1")
            .description("description1")
            .apiKeyText("apiKeyText1")
            .apiSecretText("apiSecretText1")
            .endpointText("endpointText1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static PaymentProvider getPaymentProviderSample2() {
        return new PaymentProvider()
            .id(2L)
            .providerName("providerName2")
            .description("description2")
            .apiKeyText("apiKeyText2")
            .apiSecretText("apiSecretText2")
            .endpointText("endpointText2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static PaymentProvider getPaymentProviderRandomSampleGenerator() {
        return new PaymentProvider()
            .id(longCount.incrementAndGet())
            .providerName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .apiKeyText(UUID.randomUUID().toString())
            .apiSecretText(UUID.randomUUID().toString())
            .endpointText(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
