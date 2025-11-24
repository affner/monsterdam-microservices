package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserEventDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserEventDTO.class);
        UserEventDTO userEventDTO1 = new UserEventDTO();
        userEventDTO1.setId(1L);
        UserEventDTO userEventDTO2 = new UserEventDTO();
        assertThat(userEventDTO1).isNotEqualTo(userEventDTO2);
        userEventDTO2.setId(userEventDTO1.getId());
        assertThat(userEventDTO1).isEqualTo(userEventDTO2);
        userEventDTO2.setId(2L);
        assertThat(userEventDTO1).isNotEqualTo(userEventDTO2);
        userEventDTO1.setId(null);
        assertThat(userEventDTO1).isNotEqualTo(userEventDTO2);
    }
}
