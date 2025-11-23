package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserReport getUserReportSample1() {
        return new UserReport()
            .id(1L)
            .reportDescription("reportDescription1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .reporterId(1L)
            .reportedId(1L)
            .multimediaId(1L)
            .messageId(1L)
            .postId(1L)
            .commentId(1L);
    }

    public static UserReport getUserReportSample2() {
        return new UserReport()
            .id(2L)
            .reportDescription("reportDescription2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .reporterId(2L)
            .reportedId(2L)
            .multimediaId(2L)
            .messageId(2L)
            .postId(2L)
            .commentId(2L);
    }

    public static UserReport getUserReportRandomSampleGenerator() {
        return new UserReport()
            .id(longCount.incrementAndGet())
            .reportDescription(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .reporterId(longCount.incrementAndGet())
            .reportedId(longCount.incrementAndGet())
            .multimediaId(longCount.incrementAndGet())
            .messageId(longCount.incrementAndGet())
            .postId(longCount.incrementAndGet())
            .commentId(longCount.incrementAndGet());
    }
}
