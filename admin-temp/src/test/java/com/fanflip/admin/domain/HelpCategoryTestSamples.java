package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HelpCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HelpCategory getHelpCategorySample1() {
        return new HelpCategory().id(1L).name("name1");
    }

    public static HelpCategory getHelpCategorySample2() {
        return new HelpCategory().id(2L).name("name2");
    }

    public static HelpCategory getHelpCategoryRandomSampleGenerator() {
        return new HelpCategory().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
