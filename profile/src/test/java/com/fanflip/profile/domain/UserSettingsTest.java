package com.fanflip.profile.domain;

import static com.fanflip.profile.domain.UserProfileTestSamples.*;
import static com.fanflip.profile.domain.UserSettingsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.profile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserSettingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSettings.class);
        UserSettings userSettings1 = getUserSettingsSample1();
        UserSettings userSettings2 = new UserSettings();
        assertThat(userSettings1).isNotEqualTo(userSettings2);

        userSettings2.setId(userSettings1.getId());
        assertThat(userSettings1).isEqualTo(userSettings2);

        userSettings2 = getUserSettingsSample2();
        assertThat(userSettings1).isNotEqualTo(userSettings2);
    }

    @Test
    void userProfileTest() throws Exception {
        UserSettings userSettings = getUserSettingsRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userSettings.setUserProfile(userProfileBack);
        assertThat(userSettings.getUserProfile()).isEqualTo(userProfileBack);
        assertThat(userProfileBack.getSettings()).isEqualTo(userSettings);

        userSettings.userProfile(null);
        assertThat(userSettings.getUserProfile()).isNull();
        assertThat(userProfileBack.getSettings()).isNull();
    }
}
