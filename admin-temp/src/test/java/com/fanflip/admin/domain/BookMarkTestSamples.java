package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookMarkTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BookMark getBookMarkSample1() {
        return new BookMark().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static BookMark getBookMarkSample2() {
        return new BookMark().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static BookMark getBookMarkRandomSampleGenerator() {
        return new BookMark()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
