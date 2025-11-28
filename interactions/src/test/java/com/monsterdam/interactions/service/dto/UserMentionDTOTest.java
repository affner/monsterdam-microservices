package com.monsterdam.interactions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.interactions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserMentionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMentionDTO.class);
        UserMentionDTO userMentionDTO1 = new UserMentionDTO();
        userMentionDTO1.setId(1L);
        UserMentionDTO userMentionDTO2 = new UserMentionDTO();
        assertThat(userMentionDTO1).isNotEqualTo(userMentionDTO2);
        userMentionDTO2.setId(userMentionDTO1.getId());
        assertThat(userMentionDTO1).isEqualTo(userMentionDTO2);
        userMentionDTO2.setId(2L);
        assertThat(userMentionDTO1).isNotEqualTo(userMentionDTO2);
        userMentionDTO1.setId(null);
        assertThat(userMentionDTO1).isNotEqualTo(userMentionDTO2);
    }
}
