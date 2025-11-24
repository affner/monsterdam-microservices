package com.monsterdam.interactions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserMentionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserMention getUserMentionSample1() {
        return new UserMention().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1").mentionedUserId(1L);
    }

    public static UserMention getUserMentionSample2() {
        return new UserMention().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2").mentionedUserId(2L);
    }

    public static UserMention getUserMentionRandomSampleGenerator() {
        return new UserMention()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .mentionedUserId(longCount.incrementAndGet());
    }
}
