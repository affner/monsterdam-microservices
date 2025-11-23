package com.fanflip.catalogs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GlobalEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static GlobalEvent getGlobalEventSample1() {
        return new GlobalEvent()
            .id(1L)
            .eventName("eventName1")
            .description("description1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static GlobalEvent getGlobalEventSample2() {
        return new GlobalEvent()
            .id(2L)
            .eventName("eventName2")
            .description("description2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static GlobalEvent getGlobalEventRandomSampleGenerator() {
        return new GlobalEvent()
            .id(longCount.incrementAndGet())
            .eventName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
