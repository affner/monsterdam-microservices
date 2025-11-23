package com.fanflip.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserUIPreferencesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserUIPreferencesDTO.class);
        UserUIPreferencesDTO userUIPreferencesDTO1 = new UserUIPreferencesDTO();
        userUIPreferencesDTO1.setId(1L);
        UserUIPreferencesDTO userUIPreferencesDTO2 = new UserUIPreferencesDTO();
        assertThat(userUIPreferencesDTO1).isNotEqualTo(userUIPreferencesDTO2);
        userUIPreferencesDTO2.setId(userUIPreferencesDTO1.getId());
        assertThat(userUIPreferencesDTO1).isEqualTo(userUIPreferencesDTO2);
        userUIPreferencesDTO2.setId(2L);
        assertThat(userUIPreferencesDTO1).isNotEqualTo(userUIPreferencesDTO2);
        userUIPreferencesDTO1.setId(null);
        assertThat(userUIPreferencesDTO1).isNotEqualTo(userUIPreferencesDTO2);
    }
}
