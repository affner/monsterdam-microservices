package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModerationActionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModerationActionDTO.class);
        ModerationActionDTO moderationActionDTO1 = new ModerationActionDTO();
        moderationActionDTO1.setId(1L);
        ModerationActionDTO moderationActionDTO2 = new ModerationActionDTO();
        assertThat(moderationActionDTO1).isNotEqualTo(moderationActionDTO2);
        moderationActionDTO2.setId(moderationActionDTO1.getId());
        assertThat(moderationActionDTO1).isEqualTo(moderationActionDTO2);
        moderationActionDTO2.setId(2L);
        assertThat(moderationActionDTO1).isNotEqualTo(moderationActionDTO2);
        moderationActionDTO1.setId(null);
        assertThat(moderationActionDTO1).isNotEqualTo(moderationActionDTO2);
    }
}
