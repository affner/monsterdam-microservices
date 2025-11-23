package com.fanflip.profile.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SpecialAwardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SpecialAward getSpecialAwardSample1() {
        return new SpecialAward()
            .id(1L)
            .reason("reason1")
            .altSpecialTitle("altSpecialTitle1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .specialTitleId(1L);
    }

    public static SpecialAward getSpecialAwardSample2() {
        return new SpecialAward()
            .id(2L)
            .reason("reason2")
            .altSpecialTitle("altSpecialTitle2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .specialTitleId(2L);
    }

    public static SpecialAward getSpecialAwardRandomSampleGenerator() {
        return new SpecialAward()
            .id(longCount.incrementAndGet())
            .reason(UUID.randomUUID().toString())
            .altSpecialTitle(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .specialTitleId(longCount.incrementAndGet());
    }
}
