package com.fanflip.interactions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DirectMessageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DirectMessage getDirectMessageSample1() {
        return new DirectMessage()
            .id(1L)
            .likeCount(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .repliedStoryId(1L)
            .senderUserId(1L);
    }

    public static DirectMessage getDirectMessageSample2() {
        return new DirectMessage()
            .id(2L)
            .likeCount(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .repliedStoryId(2L)
            .senderUserId(2L);
    }

    public static DirectMessage getDirectMessageRandomSampleGenerator() {
        return new DirectMessage()
            .id(longCount.incrementAndGet())
            .likeCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .repliedStoryId(longCount.incrementAndGet())
            .senderUserId(longCount.incrementAndGet());
    }
}
