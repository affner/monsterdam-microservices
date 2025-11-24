package com.monsterdam.catalogs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.catalogs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HelpCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpCategoryDTO.class);
        HelpCategoryDTO helpCategoryDTO1 = new HelpCategoryDTO();
        helpCategoryDTO1.setId(1L);
        HelpCategoryDTO helpCategoryDTO2 = new HelpCategoryDTO();
        assertThat(helpCategoryDTO1).isNotEqualTo(helpCategoryDTO2);
        helpCategoryDTO2.setId(helpCategoryDTO1.getId());
        assertThat(helpCategoryDTO1).isEqualTo(helpCategoryDTO2);
        helpCategoryDTO2.setId(2L);
        assertThat(helpCategoryDTO1).isNotEqualTo(helpCategoryDTO2);
        helpCategoryDTO1.setId(null);
        assertThat(helpCategoryDTO1).isNotEqualTo(helpCategoryDTO2);
    }
}
