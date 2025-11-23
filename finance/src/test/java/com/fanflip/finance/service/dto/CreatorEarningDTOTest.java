package com.fanflip.finance.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.finance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CreatorEarningDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreatorEarningDTO.class);
        CreatorEarningDTO creatorEarningDTO1 = new CreatorEarningDTO();
        creatorEarningDTO1.setId(1L);
        CreatorEarningDTO creatorEarningDTO2 = new CreatorEarningDTO();
        assertThat(creatorEarningDTO1).isNotEqualTo(creatorEarningDTO2);
        creatorEarningDTO2.setId(creatorEarningDTO1.getId());
        assertThat(creatorEarningDTO1).isEqualTo(creatorEarningDTO2);
        creatorEarningDTO2.setId(2L);
        assertThat(creatorEarningDTO1).isNotEqualTo(creatorEarningDTO2);
        creatorEarningDTO1.setId(null);
        assertThat(creatorEarningDTO1).isNotEqualTo(creatorEarningDTO2);
    }
}
