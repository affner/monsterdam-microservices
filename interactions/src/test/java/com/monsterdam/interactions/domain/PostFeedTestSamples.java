package com.monsterdam.interactions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PostFeedTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PostFeed getPostFeedSample1() {
        return new PostFeed().id(1L).likeCount(1).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1").creatorUserId(1L);
    }

    public static PostFeed getPostFeedSample2() {
        return new PostFeed().id(2L).likeCount(2).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2").creatorUserId(2L);
    }

    public static PostFeed getPostFeedRandomSampleGenerator() {
        return new PostFeed()
            .id(longCount.incrementAndGet())
            .likeCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .creatorUserId(longCount.incrementAndGet());
    }
}
