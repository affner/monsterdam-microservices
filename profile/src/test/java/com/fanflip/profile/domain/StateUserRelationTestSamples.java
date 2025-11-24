package com.monsterdam.profile.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StateUserRelationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StateUserRelation getStateUserRelationSample1() {
        return new StateUserRelation().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1").idState(1L);
    }

    public static StateUserRelation getStateUserRelationSample2() {
        return new StateUserRelation().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2").idState(2L);
    }

    public static StateUserRelation getStateUserRelationRandomSampleGenerator() {
        return new StateUserRelation()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .idState(longCount.incrementAndGet());
    }
}
