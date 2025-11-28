package com.monsterdam.profile.domain;

import static com.monsterdam.profile.domain.StateUserRelationTestSamples.*;
import static com.monsterdam.profile.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.profile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StateUserRelationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StateUserRelation.class);
        StateUserRelation stateUserRelation1 = getStateUserRelationSample1();
        StateUserRelation stateUserRelation2 = new StateUserRelation();
        assertThat(stateUserRelation1).isNotEqualTo(stateUserRelation2);

        stateUserRelation2.setId(stateUserRelation1.getId());
        assertThat(stateUserRelation1).isEqualTo(stateUserRelation2);

        stateUserRelation2 = getStateUserRelationSample2();
        assertThat(stateUserRelation1).isNotEqualTo(stateUserRelation2);
    }

    @Test
    void userTest() throws Exception {
        StateUserRelation stateUserRelation = getStateUserRelationRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        stateUserRelation.setUser(userProfileBack);
        assertThat(stateUserRelation.getUser()).isEqualTo(userProfileBack);

        stateUserRelation.user(null);
        assertThat(stateUserRelation.getUser()).isNull();
    }
}
