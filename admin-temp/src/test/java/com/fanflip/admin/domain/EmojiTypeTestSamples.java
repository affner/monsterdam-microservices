package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmojiTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EmojiType getEmojiTypeSample1() {
        return new EmojiType().id(1L).description("description1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static EmojiType getEmojiTypeSample2() {
        return new EmojiType().id(2L).description("description2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static EmojiType getEmojiTypeRandomSampleGenerator() {
        return new EmojiType()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
