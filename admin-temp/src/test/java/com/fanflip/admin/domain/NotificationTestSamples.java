package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Notification getNotificationSample1() {
        return new Notification()
            .id(1L)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .postCommentId(1L)
            .postFeedId(1L)
            .directMessageId(1L)
            .userMentionId(1L)
            .likeMarkId(1L);
    }

    public static Notification getNotificationSample2() {
        return new Notification()
            .id(2L)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .postCommentId(2L)
            .postFeedId(2L)
            .directMessageId(2L)
            .userMentionId(2L)
            .likeMarkId(2L);
    }

    public static Notification getNotificationRandomSampleGenerator() {
        return new Notification()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .postCommentId(longCount.incrementAndGet())
            .postFeedId(longCount.incrementAndGet())
            .directMessageId(longCount.incrementAndGet())
            .userMentionId(longCount.incrementAndGet())
            .likeMarkId(longCount.incrementAndGet());
    }
}
