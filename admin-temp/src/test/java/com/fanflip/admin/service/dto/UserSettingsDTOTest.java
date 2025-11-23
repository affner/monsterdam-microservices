package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserSettingsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSettingsDTO.class);
        UserSettingsDTO userSettingsDTO1 = new UserSettingsDTO();
        userSettingsDTO1.setId(1L);
        UserSettingsDTO userSettingsDTO2 = new UserSettingsDTO();
        assertThat(userSettingsDTO1).isNotEqualTo(userSettingsDTO2);
        userSettingsDTO2.setId(userSettingsDTO1.getId());
        assertThat(userSettingsDTO1).isEqualTo(userSettingsDTO2);
        userSettingsDTO2.setId(2L);
        assertThat(userSettingsDTO1).isNotEqualTo(userSettingsDTO2);
        userSettingsDTO1.setId(null);
        assertThat(userSettingsDTO1).isNotEqualTo(userSettingsDTO2);
    }
}
