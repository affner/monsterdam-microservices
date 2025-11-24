package com.monsterdam.profile.domain;

import static com.monsterdam.profile.domain.UserAssociationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.profile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAssociationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAssociation.class);
        UserAssociation userAssociation1 = getUserAssociationSample1();
        UserAssociation userAssociation2 = new UserAssociation();
        assertThat(userAssociation1).isNotEqualTo(userAssociation2);

        userAssociation2.setId(userAssociation1.getId());
        assertThat(userAssociation1).isEqualTo(userAssociation2);

        userAssociation2 = getUserAssociationSample2();
        assertThat(userAssociation1).isNotEqualTo(userAssociation2);
    }
}
