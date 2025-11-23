package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HelpSubcategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HelpSubcategory getHelpSubcategorySample1() {
        return new HelpSubcategory().id(1L).name("name1");
    }

    public static HelpSubcategory getHelpSubcategorySample2() {
        return new HelpSubcategory().id(2L).name("name2");
    }

    public static HelpSubcategory getHelpSubcategoryRandomSampleGenerator() {
        return new HelpSubcategory().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
