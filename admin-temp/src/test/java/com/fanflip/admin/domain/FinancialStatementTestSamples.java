package com.fanflip.admin.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FinancialStatementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FinancialStatement getFinancialStatementSample1() {
        return new FinancialStatement().id(1L);
    }

    public static FinancialStatement getFinancialStatementSample2() {
        return new FinancialStatement().id(2L);
    }

    public static FinancialStatement getFinancialStatementRandomSampleGenerator() {
        return new FinancialStatement().id(longCount.incrementAndGet());
    }
}
