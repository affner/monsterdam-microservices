package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AdminAnnouncementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AdminAnnouncement getAdminAnnouncementSample1() {
        return new AdminAnnouncement().id(1L).title("title1").content("content1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static AdminAnnouncement getAdminAnnouncementSample2() {
        return new AdminAnnouncement().id(2L).title("title2").content("content2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static AdminAnnouncement getAdminAnnouncementRandomSampleGenerator() {
        return new AdminAnnouncement()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
