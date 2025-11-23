package com.fanflip.multimedia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SinglePhotoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SinglePhoto getSinglePhotoSample1() {
        return new SinglePhoto()
            .id(1L)
            .thumbnailS3Key("thumbnailS3Key1")
            .contentS3Key("contentS3Key1")
            .likeCount(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static SinglePhoto getSinglePhotoSample2() {
        return new SinglePhoto()
            .id(2L)
            .thumbnailS3Key("thumbnailS3Key2")
            .contentS3Key("contentS3Key2")
            .likeCount(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static SinglePhoto getSinglePhotoRandomSampleGenerator() {
        return new SinglePhoto()
            .id(longCount.incrementAndGet())
            .thumbnailS3Key(UUID.randomUUID().toString())
            .contentS3Key(UUID.randomUUID().toString())
            .likeCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
