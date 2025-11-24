package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.UserAssociationTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
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

    @Test
    void ownerTest() throws Exception {
        UserAssociation userAssociation = getUserAssociationRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userAssociation.setOwner(userProfileBack);
        assertThat(userAssociation.getOwner()).isEqualTo(userProfileBack);

        userAssociation.owner(null);
        assertThat(userAssociation.getOwner()).isNull();
    }
}
