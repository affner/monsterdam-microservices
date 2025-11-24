package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PollOptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PollOption getPollOptionSample1() {
        return new PollOption().id(1L).voteCount(1);
    }

    public static PollOption getPollOptionSample2() {
        return new PollOption().id(2L).voteCount(2);
    }

    public static PollOption getPollOptionRandomSampleGenerator() {
        return new PollOption().id(longCount.incrementAndGet()).voteCount(intCount.incrementAndGet());
    }
}
