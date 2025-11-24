package com.monsterdam.profile.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.profile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalSocialLinksDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalSocialLinksDTO.class);
        PersonalSocialLinksDTO personalSocialLinksDTO1 = new PersonalSocialLinksDTO();
        personalSocialLinksDTO1.setId(1L);
        PersonalSocialLinksDTO personalSocialLinksDTO2 = new PersonalSocialLinksDTO();
        assertThat(personalSocialLinksDTO1).isNotEqualTo(personalSocialLinksDTO2);
        personalSocialLinksDTO2.setId(personalSocialLinksDTO1.getId());
        assertThat(personalSocialLinksDTO1).isEqualTo(personalSocialLinksDTO2);
        personalSocialLinksDTO2.setId(2L);
        assertThat(personalSocialLinksDTO1).isNotEqualTo(personalSocialLinksDTO2);
        personalSocialLinksDTO1.setId(null);
        assertThat(personalSocialLinksDTO1).isNotEqualTo(personalSocialLinksDTO2);
    }
}
