package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LiabilityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LiabilityDTO.class);
        LiabilityDTO liabilityDTO1 = new LiabilityDTO();
        liabilityDTO1.setId(1L);
        LiabilityDTO liabilityDTO2 = new LiabilityDTO();
        assertThat(liabilityDTO1).isNotEqualTo(liabilityDTO2);
        liabilityDTO2.setId(liabilityDTO1.getId());
        assertThat(liabilityDTO1).isEqualTo(liabilityDTO2);
        liabilityDTO2.setId(2L);
        assertThat(liabilityDTO1).isNotEqualTo(liabilityDTO2);
        liabilityDTO1.setId(null);
        assertThat(liabilityDTO1).isNotEqualTo(liabilityDTO2);
    }
}
