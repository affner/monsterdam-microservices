package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserUIPreferencesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserUIPreferences getUserUIPreferencesSample1() {
        return new UserUIPreferences().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static UserUIPreferences getUserUIPreferencesSample2() {
        return new UserUIPreferences().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static UserUIPreferences getUserUIPreferencesRandomSampleGenerator() {
        return new UserUIPreferences()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
