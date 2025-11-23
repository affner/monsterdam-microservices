package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HashTagTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HashTag getHashTagSample1() {
        return new HashTag().id(1L).tagName("tagName1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static HashTag getHashTagSample2() {
        return new HashTag().id(2L).tagName("tagName2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static HashTag getHashTagRandomSampleGenerator() {
        return new HashTag()
            .id(longCount.incrementAndGet())
            .tagName(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
