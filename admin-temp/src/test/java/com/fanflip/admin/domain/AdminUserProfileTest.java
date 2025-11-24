package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.AdminAnnouncementTestSamples.*;
import static com.monsterdam.admin.domain.AdminUserProfileTestSamples.*;
import static com.monsterdam.admin.domain.AssistanceTicketTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AdminUserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminUserProfile.class);
        AdminUserProfile adminUserProfile1 = getAdminUserProfileSample1();
        AdminUserProfile adminUserProfile2 = new AdminUserProfile();
        assertThat(adminUserProfile1).isNotEqualTo(adminUserProfile2);

        adminUserProfile2.setId(adminUserProfile1.getId());
        assertThat(adminUserProfile1).isEqualTo(adminUserProfile2);

        adminUserProfile2 = getAdminUserProfileSample2();
        assertThat(adminUserProfile1).isNotEqualTo(adminUserProfile2);
    }

    @Test
    void assignedTicketsTest() throws Exception {
        AdminUserProfile adminUserProfile = getAdminUserProfileRandomSampleGenerator();
        AssistanceTicket assistanceTicketBack = getAssistanceTicketRandomSampleGenerator();

        adminUserProfile.addAssignedTickets(assistanceTicketBack);
        assertThat(adminUserProfile.getAssignedTickets()).containsOnly(assistanceTicketBack);
        assertThat(assistanceTicketBack.getAssignedAdmin()).isEqualTo(adminUserProfile);

        adminUserProfile.removeAssignedTickets(assistanceTicketBack);
        assertThat(adminUserProfile.getAssignedTickets()).doesNotContain(assistanceTicketBack);
        assertThat(assistanceTicketBack.getAssignedAdmin()).isNull();

        adminUserProfile.assignedTickets(new HashSet<>(Set.of(assistanceTicketBack)));
        assertThat(adminUserProfile.getAssignedTickets()).containsOnly(assistanceTicketBack);
        assertThat(assistanceTicketBack.getAssignedAdmin()).isEqualTo(adminUserProfile);

        adminUserProfile.setAssignedTickets(new HashSet<>());
        assertThat(adminUserProfile.getAssignedTickets()).doesNotContain(assistanceTicketBack);
        assertThat(assistanceTicketBack.getAssignedAdmin()).isNull();
    }

    @Test
    void announcementsTest() throws Exception {
        AdminUserProfile adminUserProfile = getAdminUserProfileRandomSampleGenerator();
        AdminAnnouncement adminAnnouncementBack = getAdminAnnouncementRandomSampleGenerator();

        adminUserProfile.addAnnouncements(adminAnnouncementBack);
        assertThat(adminUserProfile.getAnnouncements()).containsOnly(adminAnnouncementBack);
        assertThat(adminAnnouncementBack.getAdmin()).isEqualTo(adminUserProfile);

        adminUserProfile.removeAnnouncements(adminAnnouncementBack);
        assertThat(adminUserProfile.getAnnouncements()).doesNotContain(adminAnnouncementBack);
        assertThat(adminAnnouncementBack.getAdmin()).isNull();

        adminUserProfile.announcements(new HashSet<>(Set.of(adminAnnouncementBack)));
        assertThat(adminUserProfile.getAnnouncements()).containsOnly(adminAnnouncementBack);
        assertThat(adminAnnouncementBack.getAdmin()).isEqualTo(adminUserProfile);

        adminUserProfile.setAnnouncements(new HashSet<>());
        assertThat(adminUserProfile.getAnnouncements()).doesNotContain(adminAnnouncementBack);
        assertThat(adminAnnouncementBack.getAdmin()).isNull();
    }
}
