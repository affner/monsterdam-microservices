package com.monsterdam.profile.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FeedbackTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Feedback getFeedbackSample1() {
        return new Feedback()
            .id(1L)
            .content("content1")
            .feedbackRating(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .creatorId(1L);
    }

    public static Feedback getFeedbackSample2() {
        return new Feedback()
            .id(2L)
            .content("content2")
            .feedbackRating(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .creatorId(2L);
    }

    public static Feedback getFeedbackRandomSampleGenerator() {
        return new Feedback()
            .id(longCount.incrementAndGet())
            .content(UUID.randomUUID().toString())
            .feedbackRating(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .creatorId(longCount.incrementAndGet());
    }
}
