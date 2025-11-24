package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CountryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Country getCountrySample1() {
        return new Country()
            .id(1L)
            .name("name1")
            .alpha2Code("alpha2Code1")
            .alpha3Code("alpha3Code1")
            .phoneCode("phoneCode1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Country getCountrySample2() {
        return new Country()
            .id(2L)
            .name("name2")
            .alpha2Code("alpha2Code2")
            .alpha3Code("alpha3Code2")
            .phoneCode("phoneCode2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Country getCountryRandomSampleGenerator() {
        return new Country()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .alpha2Code(UUID.randomUUID().toString())
            .alpha3Code(UUID.randomUUID().toString())
            .phoneCode(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
