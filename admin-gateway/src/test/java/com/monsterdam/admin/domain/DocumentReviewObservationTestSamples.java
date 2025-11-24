package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentReviewObservationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentReviewObservation getDocumentReviewObservationSample1() {
        return new DocumentReviewObservation().id(1L).comment("comment1");
    }

    public static DocumentReviewObservation getDocumentReviewObservationSample2() {
        return new DocumentReviewObservation().id(2L).comment("comment2");
    }

    public static DocumentReviewObservation getDocumentReviewObservationRandomSampleGenerator() {
        return new DocumentReviewObservation().id(longCount.incrementAndGet()).comment(UUID.randomUUID().toString());
    }
}
