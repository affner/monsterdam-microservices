package com.monsterdam.profile.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserAssociationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserAssociation getUserAssociationSample1() {
        return new UserAssociation()
            .id(1L)
            .associationToken("associationToken1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .ownerId(1L);
    }

    public static UserAssociation getUserAssociationSample2() {
        return new UserAssociation()
            .id(2L)
            .associationToken("associationToken2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .ownerId(2L);
    }

    public static UserAssociation getUserAssociationRandomSampleGenerator() {
        return new UserAssociation()
            .id(longCount.incrementAndGet())
            .associationToken(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .ownerId(longCount.incrementAndGet());
    }
}
