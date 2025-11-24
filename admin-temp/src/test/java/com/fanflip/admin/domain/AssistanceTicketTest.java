package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.AdminUserProfileTestSamples.*;
import static com.monsterdam.admin.domain.AssistanceTicketTestSamples.*;
import static com.monsterdam.admin.domain.IdentityDocumentReviewTestSamples.*;
import static com.monsterdam.admin.domain.ModerationActionTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static com.monsterdam.admin.domain.UserReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssistanceTicketTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssistanceTicket.class);
        AssistanceTicket assistanceTicket1 = getAssistanceTicketSample1();
        AssistanceTicket assistanceTicket2 = new AssistanceTicket();
        assertThat(assistanceTicket1).isNotEqualTo(assistanceTicket2);

        assistanceTicket2.setId(assistanceTicket1.getId());
        assertThat(assistanceTicket1).isEqualTo(assistanceTicket2);

        assistanceTicket2 = getAssistanceTicketSample2();
        assertThat(assistanceTicket1).isNotEqualTo(assistanceTicket2);
    }

    @Test
    void moderationActionTest() throws Exception {
        AssistanceTicket assistanceTicket = getAssistanceTicketRandomSampleGenerator();
        ModerationAction moderationActionBack = getModerationActionRandomSampleGenerator();

        assistanceTicket.setModerationAction(moderationActionBack);
        assertThat(assistanceTicket.getModerationAction()).isEqualTo(moderationActionBack);

        assistanceTicket.moderationAction(null);
        assertThat(assistanceTicket.getModerationAction()).isNull();
    }

    @Test
    void reportTest() throws Exception {
        AssistanceTicket assistanceTicket = getAssistanceTicketRandomSampleGenerator();
        UserReport userReportBack = getUserReportRandomSampleGenerator();

        assistanceTicket.setReport(userReportBack);
        assertThat(assistanceTicket.getReport()).isEqualTo(userReportBack);
        assertThat(userReportBack.getTicket()).isEqualTo(assistanceTicket);

        assistanceTicket.report(null);
        assertThat(assistanceTicket.getReport()).isNull();
        assertThat(userReportBack.getTicket()).isNull();
    }

    @Test
    void documentsReviewTest() throws Exception {
        AssistanceTicket assistanceTicket = getAssistanceTicketRandomSampleGenerator();
        IdentityDocumentReview identityDocumentReviewBack = getIdentityDocumentReviewRandomSampleGenerator();

        assistanceTicket.setDocumentsReview(identityDocumentReviewBack);
        assertThat(assistanceTicket.getDocumentsReview()).isEqualTo(identityDocumentReviewBack);
        assertThat(identityDocumentReviewBack.getTicket()).isEqualTo(assistanceTicket);

        assistanceTicket.documentsReview(null);
        assertThat(assistanceTicket.getDocumentsReview()).isNull();
        assertThat(identityDocumentReviewBack.getTicket()).isNull();
    }

    @Test
    void assignedAdminTest() throws Exception {
        AssistanceTicket assistanceTicket = getAssistanceTicketRandomSampleGenerator();
        AdminUserProfile adminUserProfileBack = getAdminUserProfileRandomSampleGenerator();

        assistanceTicket.setAssignedAdmin(adminUserProfileBack);
        assertThat(assistanceTicket.getAssignedAdmin()).isEqualTo(adminUserProfileBack);

        assistanceTicket.assignedAdmin(null);
        assertThat(assistanceTicket.getAssignedAdmin()).isNull();
    }

    @Test
    void userTest() throws Exception {
        AssistanceTicket assistanceTicket = getAssistanceTicketRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        assistanceTicket.setUser(userProfileBack);
        assertThat(assistanceTicket.getUser()).isEqualTo(userProfileBack);

        assistanceTicket.user(null);
        assertThat(assistanceTicket.getUser()).isNull();
    }
}
