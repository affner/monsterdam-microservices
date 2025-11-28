package com.monsterdam.notifications.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppNotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppNotification getAppNotificationSample1() {
        return new AppNotification()
            .id(1L)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .postCommentId(1L)
            .postFeedId(1L)
            .directMessageId(1L)
            .targetUserId(1L)
            .likeMark(1L);
    }

    public static AppNotification getAppNotificationSample2() {
        return new AppNotification()
            .id(2L)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .postCommentId(2L)
            .postFeedId(2L)
            .directMessageId(2L)
            .targetUserId(2L)
            .likeMark(2L);
    }

    public static AppNotification getAppNotificationRandomSampleGenerator() {
        return new AppNotification()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .postCommentId(longCount.incrementAndGet())
            .postFeedId(longCount.incrementAndGet())
            .directMessageId(longCount.incrementAndGet())
            .targetUserId(longCount.incrementAndGet())
            .likeMark(longCount.incrementAndGet());
    }
}
