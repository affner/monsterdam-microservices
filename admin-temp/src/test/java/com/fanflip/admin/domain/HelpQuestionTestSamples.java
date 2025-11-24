package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HelpQuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HelpQuestion getHelpQuestionSample1() {
        return new HelpQuestion().id(1L).title("title1");
    }

    public static HelpQuestion getHelpQuestionSample2() {
        return new HelpQuestion().id(2L).title("title2");
    }

    public static HelpQuestion getHelpQuestionRandomSampleGenerator() {
        return new HelpQuestion().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
