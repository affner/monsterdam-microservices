package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialRewardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialRewardDTO.class);
        SpecialRewardDTO specialRewardDTO1 = new SpecialRewardDTO();
        specialRewardDTO1.setId(1L);
        SpecialRewardDTO specialRewardDTO2 = new SpecialRewardDTO();
        assertThat(specialRewardDTO1).isNotEqualTo(specialRewardDTO2);
        specialRewardDTO2.setId(specialRewardDTO1.getId());
        assertThat(specialRewardDTO1).isEqualTo(specialRewardDTO2);
        specialRewardDTO2.setId(2L);
        assertThat(specialRewardDTO1).isNotEqualTo(specialRewardDTO2);
        specialRewardDTO1.setId(null);
        assertThat(specialRewardDTO1).isNotEqualTo(specialRewardDTO2);
    }
}
