package com.fanflip.profile.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserLiteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserLite getUserLiteSample1() {
        return new UserLite()
            .id(1L)
            .thumbnailS3Key("thumbnailS3Key1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .nickName("nickName1")
            .fullName("fullName1")
            .countryOfBirthId(1L);
    }

    public static UserLite getUserLiteSample2() {
        return new UserLite()
            .id(2L)
            .thumbnailS3Key("thumbnailS3Key2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .nickName("nickName2")
            .fullName("fullName2")
            .countryOfBirthId(2L);
    }

    public static UserLite getUserLiteRandomSampleGenerator() {
        return new UserLite()
            .id(longCount.incrementAndGet())
            .thumbnailS3Key(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .nickName(UUID.randomUUID().toString())
            .fullName(UUID.randomUUID().toString())
            .countryOfBirthId(longCount.incrementAndGet());
    }
}
