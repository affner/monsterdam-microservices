package com.fanflip.catalogs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.catalogs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialTitleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialTitleDTO.class);
        SpecialTitleDTO specialTitleDTO1 = new SpecialTitleDTO();
        specialTitleDTO1.setId(1L);
        SpecialTitleDTO specialTitleDTO2 = new SpecialTitleDTO();
        assertThat(specialTitleDTO1).isNotEqualTo(specialTitleDTO2);
        specialTitleDTO2.setId(specialTitleDTO1.getId());
        assertThat(specialTitleDTO1).isEqualTo(specialTitleDTO2);
        specialTitleDTO2.setId(2L);
        assertThat(specialTitleDTO1).isNotEqualTo(specialTitleDTO2);
        specialTitleDTO1.setId(null);
        assertThat(specialTitleDTO1).isNotEqualTo(specialTitleDTO2);
    }
}
