package com.monsterdam.profile.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.profile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialAwardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialAwardDTO.class);
        SpecialAwardDTO specialAwardDTO1 = new SpecialAwardDTO();
        specialAwardDTO1.setId(1L);
        SpecialAwardDTO specialAwardDTO2 = new SpecialAwardDTO();
        assertThat(specialAwardDTO1).isNotEqualTo(specialAwardDTO2);
        specialAwardDTO2.setId(specialAwardDTO1.getId());
        assertThat(specialAwardDTO1).isEqualTo(specialAwardDTO2);
        specialAwardDTO2.setId(2L);
        assertThat(specialAwardDTO1).isNotEqualTo(specialAwardDTO2);
        specialAwardDTO1.setId(null);
        assertThat(specialAwardDTO1).isNotEqualTo(specialAwardDTO2);
    }
}
