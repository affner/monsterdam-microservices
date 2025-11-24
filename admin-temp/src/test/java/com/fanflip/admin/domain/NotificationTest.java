package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.NotificationTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void commentedUserTest() throws Exception {
        Notification notification = getNotificationRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        notification.setCommentedUser(userProfileBack);
        assertThat(notification.getCommentedUser()).isEqualTo(userProfileBack);

        notification.commentedUser(null);
        assertThat(notification.getCommentedUser()).isNull();
    }

    @Test
    void messagedUserTest() throws Exception {
        Notification notification = getNotificationRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        notification.setMessagedUser(userProfileBack);
        assertThat(notification.getMessagedUser()).isEqualTo(userProfileBack);

        notification.messagedUser(null);
        assertThat(notification.getMessagedUser()).isNull();
    }

    @Test
    void mentionerUserInPostTest() throws Exception {
        Notification notification = getNotificationRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        notification.setMentionerUserInPost(userProfileBack);
        assertThat(notification.getMentionerUserInPost()).isEqualTo(userProfileBack);

        notification.mentionerUserInPost(null);
        assertThat(notification.getMentionerUserInPost()).isNull();
    }

    @Test
    void mentionerUserInCommentTest() throws Exception {
        Notification notification = getNotificationRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        notification.setMentionerUserInComment(userProfileBack);
        assertThat(notification.getMentionerUserInComment()).isEqualTo(userProfileBack);

        notification.mentionerUserInComment(null);
        assertThat(notification.getMentionerUserInComment()).isNull();
    }
}
