package com.fanflip.catalogs.domain;

import static com.fanflip.catalogs.domain.HelpCategoryTestSamples.*;
import static com.fanflip.catalogs.domain.HelpSubcategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.catalogs.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HelpCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpCategory.class);
        HelpCategory helpCategory1 = getHelpCategorySample1();
        HelpCategory helpCategory2 = new HelpCategory();
        assertThat(helpCategory1).isNotEqualTo(helpCategory2);

        helpCategory2.setId(helpCategory1.getId());
        assertThat(helpCategory1).isEqualTo(helpCategory2);

        helpCategory2 = getHelpCategorySample2();
        assertThat(helpCategory1).isNotEqualTo(helpCategory2);
    }

    @Test
    void subCategoriesTest() throws Exception {
        HelpCategory helpCategory = getHelpCategoryRandomSampleGenerator();
        HelpSubcategory helpSubcategoryBack = getHelpSubcategoryRandomSampleGenerator();

        helpCategory.addSubCategories(helpSubcategoryBack);
        assertThat(helpCategory.getSubCategories()).containsOnly(helpSubcategoryBack);
        assertThat(helpSubcategoryBack.getCategory()).isEqualTo(helpCategory);

        helpCategory.removeSubCategories(helpSubcategoryBack);
        assertThat(helpCategory.getSubCategories()).doesNotContain(helpSubcategoryBack);
        assertThat(helpSubcategoryBack.getCategory()).isNull();

        helpCategory.subCategories(new HashSet<>(Set.of(helpSubcategoryBack)));
        assertThat(helpCategory.getSubCategories()).containsOnly(helpSubcategoryBack);
        assertThat(helpSubcategoryBack.getCategory()).isEqualTo(helpCategory);

        helpCategory.setSubCategories(new HashSet<>());
        assertThat(helpCategory.getSubCategories()).doesNotContain(helpSubcategoryBack);
        assertThat(helpSubcategoryBack.getCategory()).isNull();
    }
}
