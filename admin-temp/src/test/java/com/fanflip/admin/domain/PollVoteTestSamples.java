package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PollVoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PollVote getPollVoteSample1() {
        return new PollVote().id(1L);
    }

    public static PollVote getPollVoteSample2() {
        return new PollVote().id(2L);
    }

    public static PollVote getPollVoteRandomSampleGenerator() {
        return new PollVote().id(longCount.incrementAndGet());
    }
}
