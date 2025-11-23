package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.UserLiteTestSamples.*;
import static com.fanflip.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserLiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserLite.class);
        UserLite userLite1 = getUserLiteSample1();
        UserLite userLite2 = new UserLite();
        assertThat(userLite1).isNotEqualTo(userLite2);

        userLite2.setId(userLite1.getId());
        assertThat(userLite1).isEqualTo(userLite2);

        userLite2 = getUserLiteSample2();
        assertThat(userLite1).isNotEqualTo(userLite2);
    }

    @Test
    void userProfileTest() throws Exception {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userLite.setUserProfile(userProfileBack);
        assertThat(userLite.getUserProfile()).isEqualTo(userProfileBack);
        assertThat(userProfileBack.getUserLite()).isEqualTo(userLite);

        userLite.userProfile(null);
        assertThat(userLite.getUserProfile()).isNull();
        assertThat(userProfileBack.getUserLite()).isNull();
    }
}
