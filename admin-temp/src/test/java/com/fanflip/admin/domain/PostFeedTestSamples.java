package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PostFeedTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PostFeed getPostFeedSample1() {
        return new PostFeed().id(1L).likeCount(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static PostFeed getPostFeedSample2() {
        return new PostFeed().id(2L).likeCount(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static PostFeed getPostFeedRandomSampleGenerator() {
        return new PostFeed()
            .id(longCount.incrementAndGet())
            .likeCount(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
