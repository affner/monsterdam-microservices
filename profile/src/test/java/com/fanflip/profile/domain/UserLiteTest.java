package com.fanflip.profile.domain;

import static com.fanflip.profile.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.profile.web.rest.TestUtil;
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
}
