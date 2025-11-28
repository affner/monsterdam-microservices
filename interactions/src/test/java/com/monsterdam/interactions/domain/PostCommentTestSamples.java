package com.monsterdam.interactions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PostCommentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PostComment getPostCommentSample1() {
        return new PostComment().id(1L).likeCount(1).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1").commenterUserId(1L);
    }

    public static PostComment getPostCommentSample2() {
        return new PostComment().id(2L).likeCount(2).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2").commenterUserId(2L);
    }

    public static PostComment getPostCommentRandomSampleGenerator() {
        return new PostComment()
            .id(longCount.incrementAndGet())
            .likeCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .commenterUserId(longCount.incrementAndGet());
    }
}
