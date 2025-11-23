package com.fanflip.interactions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LikeMarkTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LikeMark getLikeMarkSample1() {
        return new LikeMark()
            .id(1L)
            .emojiTypeId(1L)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .multimediaId(1L)
            .messageId(1L)
            .postId(1L)
            .commentId(1L)
            .likerUserId(1L);
    }

    public static LikeMark getLikeMarkSample2() {
        return new LikeMark()
            .id(2L)
            .emojiTypeId(2L)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .multimediaId(2L)
            .messageId(2L)
            .postId(2L)
            .commentId(2L)
            .likerUserId(2L);
    }

    public static LikeMark getLikeMarkRandomSampleGenerator() {
        return new LikeMark()
            .id(longCount.incrementAndGet())
            .emojiTypeId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .multimediaId(longCount.incrementAndGet())
            .messageId(longCount.incrementAndGet())
            .postId(longCount.incrementAndGet())
            .commentId(longCount.incrementAndGet())
            .likerUserId(longCount.incrementAndGet());
    }
}
