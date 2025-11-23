package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.CountryTestSamples.*;
import static com.fanflip.admin.domain.StateTestSamples.*;
import static com.fanflip.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(State.class);
        State state1 = getStateSample1();
        State state2 = new State();
        assertThat(state1).isNotEqualTo(state2);

        state2.setId(state1.getId());
        assertThat(state1).isEqualTo(state2);

        state2 = getStateSample2();
        assertThat(state1).isNotEqualTo(state2);
    }

    @Test
    void countryTest() throws Exception {
        State state = getStateRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        state.setCountry(countryBack);
        assertThat(state.getCountry()).isEqualTo(countryBack);

        state.country(null);
        assertThat(state.getCountry()).isNull();
    }

    @Test
    void blockersTest() throws Exception {
        State state = getStateRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        state.addBlockers(userProfileBack);
        assertThat(state.getBlockers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getBlockedUbications()).containsOnly(state);

        state.removeBlockers(userProfileBack);
        assertThat(state.getBlockers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getBlockedUbications()).doesNotContain(state);

        state.blockers(new HashSet<>(Set.of(userProfileBack)));
        assertThat(state.getBlockers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getBlockedUbications()).containsOnly(state);

        state.setBlockers(new HashSet<>());
        assertThat(state.getBlockers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getBlockedUbications()).doesNotContain(state);
    }
}
