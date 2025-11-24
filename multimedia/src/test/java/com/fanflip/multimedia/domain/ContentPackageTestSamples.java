package com.monsterdam.multimedia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ContentPackageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ContentPackage getContentPackageSample1() {
        return new ContentPackage()
            .id(1L)
            .videoCount(1)
            .imageCount(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .messageId(1L)
            .postId(1L);
    }

    public static ContentPackage getContentPackageSample2() {
        return new ContentPackage()
            .id(2L)
            .videoCount(2)
            .imageCount(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .messageId(2L)
            .postId(2L);
    }

    public static ContentPackage getContentPackageRandomSampleGenerator() {
        return new ContentPackage()
            .id(longCount.incrementAndGet())
            .videoCount(intCount.incrementAndGet())
            .imageCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .messageId(longCount.incrementAndGet())
            .postId(longCount.incrementAndGet());
    }
}
