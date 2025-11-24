package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PostPollTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PostPoll getPostPollSample1() {
        return new PostPoll().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static PostPoll getPostPollSample2() {
        return new PostPoll().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static PostPoll getPostPollRandomSampleGenerator() {
        return new PostPoll()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
