package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialNetworkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialNetworkDTO.class);
        SocialNetworkDTO socialNetworkDTO1 = new SocialNetworkDTO();
        socialNetworkDTO1.setId(1L);
        SocialNetworkDTO socialNetworkDTO2 = new SocialNetworkDTO();
        assertThat(socialNetworkDTO1).isNotEqualTo(socialNetworkDTO2);
        socialNetworkDTO2.setId(socialNetworkDTO1.getId());
        assertThat(socialNetworkDTO1).isEqualTo(socialNetworkDTO2);
        socialNetworkDTO2.setId(2L);
        assertThat(socialNetworkDTO1).isNotEqualTo(socialNetworkDTO2);
        socialNetworkDTO1.setId(null);
        assertThat(socialNetworkDTO1).isNotEqualTo(socialNetworkDTO2);
    }
}
