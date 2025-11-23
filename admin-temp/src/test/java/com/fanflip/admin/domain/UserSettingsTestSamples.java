package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserSettingsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserSettings getUserSettingsSample1() {
        return new UserSettings()
            .id(1L)
            .messageBlurIntensity(1)
            .sessionsActiveCount(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static UserSettings getUserSettingsSample2() {
        return new UserSettings()
            .id(2L)
            .messageBlurIntensity(2)
            .sessionsActiveCount(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static UserSettings getUserSettingsRandomSampleGenerator() {
        return new UserSettings()
            .id(longCount.incrementAndGet())
            .messageBlurIntensity(intCount.incrementAndGet())
            .sessionsActiveCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
