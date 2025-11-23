package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HelpSubcategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpSubcategoryDTO.class);
        HelpSubcategoryDTO helpSubcategoryDTO1 = new HelpSubcategoryDTO();
        helpSubcategoryDTO1.setId(1L);
        HelpSubcategoryDTO helpSubcategoryDTO2 = new HelpSubcategoryDTO();
        assertThat(helpSubcategoryDTO1).isNotEqualTo(helpSubcategoryDTO2);
        helpSubcategoryDTO2.setId(helpSubcategoryDTO1.getId());
        assertThat(helpSubcategoryDTO1).isEqualTo(helpSubcategoryDTO2);
        helpSubcategoryDTO2.setId(2L);
        assertThat(helpSubcategoryDTO1).isNotEqualTo(helpSubcategoryDTO2);
        helpSubcategoryDTO1.setId(null);
        assertThat(helpSubcategoryDTO1).isNotEqualTo(helpSubcategoryDTO2);
    }
}
