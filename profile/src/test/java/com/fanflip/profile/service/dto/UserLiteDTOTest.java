package com.monsterdam.profile.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.profile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserLiteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserLiteDTO.class);
        UserLiteDTO userLiteDTO1 = new UserLiteDTO();
        userLiteDTO1.setId(1L);
        UserLiteDTO userLiteDTO2 = new UserLiteDTO();
        assertThat(userLiteDTO1).isNotEqualTo(userLiteDTO2);
        userLiteDTO2.setId(userLiteDTO1.getId());
        assertThat(userLiteDTO1).isEqualTo(userLiteDTO2);
        userLiteDTO2.setId(2L);
        assertThat(userLiteDTO1).isNotEqualTo(userLiteDTO2);
        userLiteDTO1.setId(null);
        assertThat(userLiteDTO1).isNotEqualTo(userLiteDTO2);
    }
}
