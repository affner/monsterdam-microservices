package com.monsterdam.multimedia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SingleDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SingleDocument getSingleDocumentSample1() {
        return new SingleDocument()
            .id(1L)
            .title("title1")
            .description("description1")
            .thumbnailS3Key("thumbnailS3Key1")
            .contentS3Key("contentS3Key1")
            .likeCount(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .creatorId(1L);
    }

    public static SingleDocument getSingleDocumentSample2() {
        return new SingleDocument()
            .id(2L)
            .title("title2")
            .description("description2")
            .thumbnailS3Key("thumbnailS3Key2")
            .contentS3Key("contentS3Key2")
            .likeCount(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .creatorId(2L);
    }

    public static SingleDocument getSingleDocumentRandomSampleGenerator() {
        return new SingleDocument()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .thumbnailS3Key(UUID.randomUUID().toString())
            .contentS3Key(UUID.randomUUID().toString())
            .likeCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .creatorId(longCount.incrementAndGet());
    }
}
