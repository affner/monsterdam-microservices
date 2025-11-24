package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.AssistanceTicketTestSamples.*;
import static com.monsterdam.admin.domain.UserReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserReport.class);
        UserReport userReport1 = getUserReportSample1();
        UserReport userReport2 = new UserReport();
        assertThat(userReport1).isNotEqualTo(userReport2);

        userReport2.setId(userReport1.getId());
        assertThat(userReport1).isEqualTo(userReport2);

        userReport2 = getUserReportSample2();
        assertThat(userReport1).isNotEqualTo(userReport2);
    }

    @Test
    void ticketTest() throws Exception {
        UserReport userReport = getUserReportRandomSampleGenerator();
        AssistanceTicket assistanceTicketBack = getAssistanceTicketRandomSampleGenerator();

        userReport.setTicket(assistanceTicketBack);
        assertThat(userReport.getTicket()).isEqualTo(assistanceTicketBack);

        userReport.ticket(null);
        assertThat(userReport.getTicket()).isNull();
    }
}
