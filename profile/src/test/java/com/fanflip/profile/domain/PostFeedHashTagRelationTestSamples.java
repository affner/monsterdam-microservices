package com.fanflip.profile.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PostFeedHashTagRelationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PostFeedHashTagRelation getPostFeedHashTagRelationSample1() {
        return new PostFeedHashTagRelation().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1").postFeedId(1L);
    }

    public static PostFeedHashTagRelation getPostFeedHashTagRelationSample2() {
        return new PostFeedHashTagRelation().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2").postFeedId(2L);
    }

    public static PostFeedHashTagRelation getPostFeedHashTagRelationRandomSampleGenerator() {
        return new PostFeedHashTagRelation()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .postFeedId(longCount.incrementAndGet());
    }
}
