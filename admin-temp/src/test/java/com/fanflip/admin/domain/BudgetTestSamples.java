package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BudgetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Budget getBudgetSample1() {
        return new Budget().id(1L).year(1).budgetDetails("budgetDetails1");
    }

    public static Budget getBudgetSample2() {
        return new Budget().id(2L).year(2).budgetDetails("budgetDetails2");
    }

    public static Budget getBudgetRandomSampleGenerator() {
        return new Budget().id(longCount.incrementAndGet()).year(intCount.incrementAndGet()).budgetDetails(UUID.randomUUID().toString());
    }
}
