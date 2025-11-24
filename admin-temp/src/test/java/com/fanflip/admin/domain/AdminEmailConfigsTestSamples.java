package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AdminEmailConfigsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AdminEmailConfigs getAdminEmailConfigsSample1() {
        return new AdminEmailConfigs()
            .id(1L)
            .title("title1")
            .subject("subject1")
            .content("content1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static AdminEmailConfigs getAdminEmailConfigsSample2() {
        return new AdminEmailConfigs()
            .id(2L)
            .title("title2")
            .subject("subject2")
            .content("content2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static AdminEmailConfigs getAdminEmailConfigsRandomSampleGenerator() {
        return new AdminEmailConfigs()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .subject(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
