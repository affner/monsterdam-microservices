package com.monsterdam.catalogs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AdminSystemConfigsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AdminSystemConfigs getAdminSystemConfigsSample1() {
        return new AdminSystemConfigs()
            .id(1L)
            .configKey("configKey1")
            .configValue("configValue1")
            .description("description1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static AdminSystemConfigs getAdminSystemConfigsSample2() {
        return new AdminSystemConfigs()
            .id(2L)
            .configKey("configKey2")
            .configValue("configValue2")
            .description("description2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static AdminSystemConfigs getAdminSystemConfigsRandomSampleGenerator() {
        return new AdminSystemConfigs()
            .id(longCount.incrementAndGet())
            .configKey(UUID.randomUUID().toString())
            .configValue(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
