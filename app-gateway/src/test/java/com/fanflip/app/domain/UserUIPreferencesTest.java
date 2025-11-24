package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.UserUIPreferencesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserUIPreferencesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserUIPreferences.class);
        UserUIPreferences userUIPreferences1 = getUserUIPreferencesSample1();
        UserUIPreferences userUIPreferences2 = new UserUIPreferences();
        assertThat(userUIPreferences1).isNotEqualTo(userUIPreferences2);

        userUIPreferences2.setId(userUIPreferences1.getId());
        assertThat(userUIPreferences1).isEqualTo(userUIPreferences2);

        userUIPreferences2 = getUserUIPreferencesSample2();
        assertThat(userUIPreferences1).isNotEqualTo(userUIPreferences2);
    }
}
