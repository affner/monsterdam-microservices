package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SingleVideoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SingleVideo getSingleVideoSample1() {
        return new SingleVideo()
            .id(1L)
            .thumbnailS3Key("thumbnailS3Key1")
            .contentS3Key("contentS3Key1")
            .likeCount(1L)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static SingleVideo getSingleVideoSample2() {
        return new SingleVideo()
            .id(2L)
            .thumbnailS3Key("thumbnailS3Key2")
            .contentS3Key("contentS3Key2")
            .likeCount(2L)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static SingleVideo getSingleVideoRandomSampleGenerator() {
        return new SingleVideo()
            .id(longCount.incrementAndGet())
            .thumbnailS3Key(UUID.randomUUID().toString())
            .contentS3Key(UUID.randomUUID().toString())
            .likeCount(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
