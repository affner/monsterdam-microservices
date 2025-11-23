package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.UserEventTestSamples.*;
import static com.fanflip.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserEvent.class);
        UserEvent userEvent1 = getUserEventSample1();
        UserEvent userEvent2 = new UserEvent();
        assertThat(userEvent1).isNotEqualTo(userEvent2);

        userEvent2.setId(userEvent1.getId());
        assertThat(userEvent1).isEqualTo(userEvent2);

        userEvent2 = getUserEventSample2();
        assertThat(userEvent1).isNotEqualTo(userEvent2);
    }

    @Test
    void creatorTest() throws Exception {
        UserEvent userEvent = getUserEventRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userEvent.setCreator(userProfileBack);
        assertThat(userEvent.getCreator()).isEqualTo(userProfileBack);

        userEvent.creator(null);
        assertThat(userEvent.getCreator()).isNull();
    }

    @Test
    void membersTest() throws Exception {
        UserEvent userEvent = getUserEventRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userEvent.addMembers(userProfileBack);
        assertThat(userEvent.getMembers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getJoinedEvents()).containsOnly(userEvent);

        userEvent.removeMembers(userProfileBack);
        assertThat(userEvent.getMembers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getJoinedEvents()).doesNotContain(userEvent);

        userEvent.members(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userEvent.getMembers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getJoinedEvents()).containsOnly(userEvent);

        userEvent.setMembers(new HashSet<>());
        assertThat(userEvent.getMembers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getJoinedEvents()).doesNotContain(userEvent);
    }
}
