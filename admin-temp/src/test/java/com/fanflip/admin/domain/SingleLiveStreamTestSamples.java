package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SingleLiveStreamTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SingleLiveStream getSingleLiveStreamSample1() {
        return new SingleLiveStream()
            .id(1L)
            .title("title1")
            .description("description1")
            .thumbnailS3Key("thumbnailS3Key1")
            .liveContentS3Key("liveContentS3Key1")
            .likeCount(1L)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static SingleLiveStream getSingleLiveStreamSample2() {
        return new SingleLiveStream()
            .id(2L)
            .title("title2")
            .description("description2")
            .thumbnailS3Key("thumbnailS3Key2")
            .liveContentS3Key("liveContentS3Key2")
            .likeCount(2L)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static SingleLiveStream getSingleLiveStreamRandomSampleGenerator() {
        return new SingleLiveStream()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .thumbnailS3Key(UUID.randomUUID().toString())
            .liveContentS3Key(UUID.randomUUID().toString())
            .likeCount(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
