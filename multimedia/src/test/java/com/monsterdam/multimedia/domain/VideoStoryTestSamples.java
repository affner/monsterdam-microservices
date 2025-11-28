package com.monsterdam.multimedia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VideoStoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static VideoStory getVideoStorySample1() {
        return new VideoStory()
            .id(1L)
            .thumbnailS3Key("thumbnailS3Key1")
            .contentS3Key("contentS3Key1")
            .likeCount(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .creatorId(1L);
    }

    public static VideoStory getVideoStorySample2() {
        return new VideoStory()
            .id(2L)
            .thumbnailS3Key("thumbnailS3Key2")
            .contentS3Key("contentS3Key2")
            .likeCount(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .creatorId(2L);
    }

    public static VideoStory getVideoStoryRandomSampleGenerator() {
        return new VideoStory()
            .id(longCount.incrementAndGet())
            .thumbnailS3Key(UUID.randomUUID().toString())
            .contentS3Key(UUID.randomUUID().toString())
            .likeCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .creatorId(longCount.incrementAndGet());
    }
}
