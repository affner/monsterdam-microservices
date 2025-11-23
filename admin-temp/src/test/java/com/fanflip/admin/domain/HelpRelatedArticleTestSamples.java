package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HelpRelatedArticleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HelpRelatedArticle getHelpRelatedArticleSample1() {
        return new HelpRelatedArticle().id(1L).title("title1");
    }

    public static HelpRelatedArticle getHelpRelatedArticleSample2() {
        return new HelpRelatedArticle().id(2L).title("title2");
    }

    public static HelpRelatedArticle getHelpRelatedArticleRandomSampleGenerator() {
        return new HelpRelatedArticle().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
