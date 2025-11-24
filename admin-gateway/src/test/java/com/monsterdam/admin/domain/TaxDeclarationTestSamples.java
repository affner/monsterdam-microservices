package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TaxDeclarationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TaxDeclaration getTaxDeclarationSample1() {
        return new TaxDeclaration().id(1L).year(1).supportingDocumentsKey("supportingDocumentsKey1");
    }

    public static TaxDeclaration getTaxDeclarationSample2() {
        return new TaxDeclaration().id(2L).year(2).supportingDocumentsKey("supportingDocumentsKey2");
    }

    public static TaxDeclaration getTaxDeclarationRandomSampleGenerator() {
        return new TaxDeclaration()
            .id(longCount.incrementAndGet())
            .year(intCount.incrementAndGet())
            .supportingDocumentsKey(UUID.randomUUID().toString());
    }
}
