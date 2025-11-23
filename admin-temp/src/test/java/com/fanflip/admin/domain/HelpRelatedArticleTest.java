package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.HelpQuestionTestSamples.*;
import static com.fanflip.admin.domain.HelpRelatedArticleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HelpRelatedArticleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpRelatedArticle.class);
        HelpRelatedArticle helpRelatedArticle1 = getHelpRelatedArticleSample1();
        HelpRelatedArticle helpRelatedArticle2 = new HelpRelatedArticle();
        assertThat(helpRelatedArticle1).isNotEqualTo(helpRelatedArticle2);

        helpRelatedArticle2.setId(helpRelatedArticle1.getId());
        assertThat(helpRelatedArticle1).isEqualTo(helpRelatedArticle2);

        helpRelatedArticle2 = getHelpRelatedArticleSample2();
        assertThat(helpRelatedArticle1).isNotEqualTo(helpRelatedArticle2);
    }

    @Test
    void relatedArticleTest() throws Exception {
        HelpRelatedArticle helpRelatedArticle = getHelpRelatedArticleRandomSampleGenerator();
        HelpQuestion helpQuestionBack = getHelpQuestionRandomSampleGenerator();

        helpRelatedArticle.addRelatedArticle(helpQuestionBack);
        assertThat(helpRelatedArticle.getRelatedArticles()).containsOnly(helpQuestionBack);
        assertThat(helpQuestionBack.getQuestions()).containsOnly(helpRelatedArticle);

        helpRelatedArticle.removeRelatedArticle(helpQuestionBack);
        assertThat(helpRelatedArticle.getRelatedArticles()).doesNotContain(helpQuestionBack);
        assertThat(helpQuestionBack.getQuestions()).doesNotContain(helpRelatedArticle);

        helpRelatedArticle.relatedArticles(new HashSet<>(Set.of(helpQuestionBack)));
        assertThat(helpRelatedArticle.getRelatedArticles()).containsOnly(helpQuestionBack);
        assertThat(helpQuestionBack.getQuestions()).containsOnly(helpRelatedArticle);

        helpRelatedArticle.setRelatedArticles(new HashSet<>());
        assertThat(helpRelatedArticle.getRelatedArticles()).doesNotContain(helpQuestionBack);
        assertThat(helpQuestionBack.getQuestions()).doesNotContain(helpRelatedArticle);
    }
}
