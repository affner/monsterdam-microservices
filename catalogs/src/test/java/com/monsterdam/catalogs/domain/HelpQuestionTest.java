package com.monsterdam.catalogs.domain;

import static com.monsterdam.catalogs.domain.HelpQuestionTestSamples.*;
import static com.monsterdam.catalogs.domain.HelpRelatedArticleTestSamples.*;
import static com.monsterdam.catalogs.domain.HelpSubcategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.catalogs.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HelpQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpQuestion.class);
        HelpQuestion helpQuestion1 = getHelpQuestionSample1();
        HelpQuestion helpQuestion2 = new HelpQuestion();
        assertThat(helpQuestion1).isNotEqualTo(helpQuestion2);

        helpQuestion2.setId(helpQuestion1.getId());
        assertThat(helpQuestion1).isEqualTo(helpQuestion2);

        helpQuestion2 = getHelpQuestionSample2();
        assertThat(helpQuestion1).isNotEqualTo(helpQuestion2);
    }

    @Test
    void questionTest() throws Exception {
        HelpQuestion helpQuestion = getHelpQuestionRandomSampleGenerator();
        HelpRelatedArticle helpRelatedArticleBack = getHelpRelatedArticleRandomSampleGenerator();

        helpQuestion.addQuestion(helpRelatedArticleBack);
        assertThat(helpQuestion.getQuestions()).containsOnly(helpRelatedArticleBack);

        helpQuestion.removeQuestion(helpRelatedArticleBack);
        assertThat(helpQuestion.getQuestions()).doesNotContain(helpRelatedArticleBack);

        helpQuestion.questions(new HashSet<>(Set.of(helpRelatedArticleBack)));
        assertThat(helpQuestion.getQuestions()).containsOnly(helpRelatedArticleBack);

        helpQuestion.setQuestions(new HashSet<>());
        assertThat(helpQuestion.getQuestions()).doesNotContain(helpRelatedArticleBack);
    }

    @Test
    void subcategoryTest() throws Exception {
        HelpQuestion helpQuestion = getHelpQuestionRandomSampleGenerator();
        HelpSubcategory helpSubcategoryBack = getHelpSubcategoryRandomSampleGenerator();

        helpQuestion.setSubcategory(helpSubcategoryBack);
        assertThat(helpQuestion.getSubcategory()).isEqualTo(helpSubcategoryBack);

        helpQuestion.subcategory(null);
        assertThat(helpQuestion.getSubcategory()).isNull();
    }
}
