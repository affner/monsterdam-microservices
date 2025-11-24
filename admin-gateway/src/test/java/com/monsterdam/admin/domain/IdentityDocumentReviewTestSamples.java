package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IdentityDocumentReviewTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IdentityDocumentReview getIdentityDocumentReviewSample1() {
        return new IdentityDocumentReview().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static IdentityDocumentReview getIdentityDocumentReviewSample2() {
        return new IdentityDocumentReview().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static IdentityDocumentReview getIdentityDocumentReviewRandomSampleGenerator() {
        return new IdentityDocumentReview()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
