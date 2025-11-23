package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DirectMessageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DirectMessage getDirectMessageSample1() {
        return new DirectMessage().id(1L).likeCount(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static DirectMessage getDirectMessageSample2() {
        return new DirectMessage().id(2L).likeCount(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static DirectMessage getDirectMessageRandomSampleGenerator() {
        return new DirectMessage()
            .id(longCount.incrementAndGet())
            .likeCount(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
