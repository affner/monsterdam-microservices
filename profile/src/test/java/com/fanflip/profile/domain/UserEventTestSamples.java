package com.monsterdam.profile.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserEvent getUserEventSample1() {
        return new UserEvent().id(1L).title("title1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1").creatorId(1L);
    }

    public static UserEvent getUserEventSample2() {
        return new UserEvent().id(2L).title("title2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2").creatorId(2L);
    }

    public static UserEvent getUserEventRandomSampleGenerator() {
        return new UserEvent()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .creatorId(longCount.incrementAndGet());
    }
}
