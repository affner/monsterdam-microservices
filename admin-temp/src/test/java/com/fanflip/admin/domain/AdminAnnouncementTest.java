package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.AdminAnnouncementTestSamples.*;
import static com.monsterdam.admin.domain.AdminUserProfileTestSamples.*;
import static com.monsterdam.admin.domain.DirectMessageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdminAnnouncementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminAnnouncement.class);
        AdminAnnouncement adminAnnouncement1 = getAdminAnnouncementSample1();
        AdminAnnouncement adminAnnouncement2 = new AdminAnnouncement();
        assertThat(adminAnnouncement1).isNotEqualTo(adminAnnouncement2);

        adminAnnouncement2.setId(adminAnnouncement1.getId());
        assertThat(adminAnnouncement1).isEqualTo(adminAnnouncement2);

        adminAnnouncement2 = getAdminAnnouncementSample2();
        assertThat(adminAnnouncement1).isNotEqualTo(adminAnnouncement2);
    }

    @Test
    void announcerMessageTest() throws Exception {
        AdminAnnouncement adminAnnouncement = getAdminAnnouncementRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        adminAnnouncement.setAnnouncerMessage(directMessageBack);
        assertThat(adminAnnouncement.getAnnouncerMessage()).isEqualTo(directMessageBack);

        adminAnnouncement.announcerMessage(null);
        assertThat(adminAnnouncement.getAnnouncerMessage()).isNull();
    }

    @Test
    void adminTest() throws Exception {
        AdminAnnouncement adminAnnouncement = getAdminAnnouncementRandomSampleGenerator();
        AdminUserProfile adminUserProfileBack = getAdminUserProfileRandomSampleGenerator();

        adminAnnouncement.setAdmin(adminUserProfileBack);
        assertThat(adminAnnouncement.getAdmin()).isEqualTo(adminUserProfileBack);

        adminAnnouncement.admin(null);
        assertThat(adminAnnouncement.getAdmin()).isNull();
    }
}
