package com.fanflip.profile.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.profile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAssociationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAssociationDTO.class);
        UserAssociationDTO userAssociationDTO1 = new UserAssociationDTO();
        userAssociationDTO1.setId(1L);
        UserAssociationDTO userAssociationDTO2 = new UserAssociationDTO();
        assertThat(userAssociationDTO1).isNotEqualTo(userAssociationDTO2);
        userAssociationDTO2.setId(userAssociationDTO1.getId());
        assertThat(userAssociationDTO1).isEqualTo(userAssociationDTO2);
        userAssociationDTO2.setId(2L);
        assertThat(userAssociationDTO1).isNotEqualTo(userAssociationDTO2);
        userAssociationDTO1.setId(null);
        assertThat(userAssociationDTO1).isNotEqualTo(userAssociationDTO2);
    }
}
