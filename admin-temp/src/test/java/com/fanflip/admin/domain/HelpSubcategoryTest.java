package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.HelpCategoryTestSamples.*;
import static com.fanflip.admin.domain.HelpQuestionTestSamples.*;
import static com.fanflip.admin.domain.HelpSubcategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HelpSubcategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpSubcategory.class);
        HelpSubcategory helpSubcategory1 = getHelpSubcategorySample1();
        HelpSubcategory helpSubcategory2 = new HelpSubcategory();
        assertThat(helpSubcategory1).isNotEqualTo(helpSubcategory2);

        helpSubcategory2.setId(helpSubcategory1.getId());
        assertThat(helpSubcategory1).isEqualTo(helpSubcategory2);

        helpSubcategory2 = getHelpSubcategorySample2();
        assertThat(helpSubcategory1).isNotEqualTo(helpSubcategory2);
    }

    @Test
    void questionsTest() throws Exception {
        HelpSubcategory helpSubcategory = getHelpSubcategoryRandomSampleGenerator();
        HelpQuestion helpQuestionBack = getHelpQuestionRandomSampleGenerator();

        helpSubcategory.addQuestions(helpQuestionBack);
        assertThat(helpSubcategory.getQuestions()).containsOnly(helpQuestionBack);
        assertThat(helpQuestionBack.getSubcategory()).isEqualTo(helpSubcategory);

        helpSubcategory.removeQuestions(helpQuestionBack);
        assertThat(helpSubcategory.getQuestions()).doesNotContain(helpQuestionBack);
        assertThat(helpQuestionBack.getSubcategory()).isNull();

        helpSubcategory.questions(new HashSet<>(Set.of(helpQuestionBack)));
        assertThat(helpSubcategory.getQuestions()).containsOnly(helpQuestionBack);
        assertThat(helpQuestionBack.getSubcategory()).isEqualTo(helpSubcategory);

        helpSubcategory.setQuestions(new HashSet<>());
        assertThat(helpSubcategory.getQuestions()).doesNotContain(helpQuestionBack);
        assertThat(helpQuestionBack.getSubcategory()).isNull();
    }

    @Test
    void categoryTest() throws Exception {
        HelpSubcategory helpSubcategory = getHelpSubcategoryRandomSampleGenerator();
        HelpCategory helpCategoryBack = getHelpCategoryRandomSampleGenerator();

        helpSubcategory.setCategory(helpCategoryBack);
        assertThat(helpSubcategory.getCategory()).isEqualTo(helpCategoryBack);

        helpSubcategory.category(null);
        assertThat(helpSubcategory.getCategory()).isNull();
    }
}
