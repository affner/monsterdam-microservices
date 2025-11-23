package com.fanflip.multimedia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserTagRelationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserTagRelation getUserTagRelationSample1() {
        return new UserTagRelation().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1").taggedUserId(1L);
    }

    public static UserTagRelation getUserTagRelationSample2() {
        return new UserTagRelation().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2").taggedUserId(2L);
    }

    public static UserTagRelation getUserTagRelationRandomSampleGenerator() {
        return new UserTagRelation()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .taggedUserId(longCount.incrementAndGet());
    }
}
