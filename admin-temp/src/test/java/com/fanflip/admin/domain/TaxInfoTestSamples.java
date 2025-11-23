package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TaxInfoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TaxInfo getTaxInfoSample1() {
        return new TaxInfo().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static TaxInfo getTaxInfoSample2() {
        return new TaxInfo().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static TaxInfo getTaxInfoRandomSampleGenerator() {
        return new TaxInfo()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
