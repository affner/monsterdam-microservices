package com.monsterdam.profile.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.profile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HashTagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HashTagDTO.class);
        HashTagDTO hashTagDTO1 = new HashTagDTO();
        hashTagDTO1.setId(1L);
        HashTagDTO hashTagDTO2 = new HashTagDTO();
        assertThat(hashTagDTO1).isNotEqualTo(hashTagDTO2);
        hashTagDTO2.setId(hashTagDTO1.getId());
        assertThat(hashTagDTO1).isEqualTo(hashTagDTO2);
        hashTagDTO2.setId(2L);
        assertThat(hashTagDTO1).isNotEqualTo(hashTagDTO2);
        hashTagDTO1.setId(null);
        assertThat(hashTagDTO1).isNotEqualTo(hashTagDTO2);
    }
}
