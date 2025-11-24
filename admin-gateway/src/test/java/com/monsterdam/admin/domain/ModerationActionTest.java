package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.AssistanceTicketTestSamples.*;
import static com.monsterdam.admin.domain.ModerationActionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModerationActionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModerationAction.class);
        ModerationAction moderationAction1 = getModerationActionSample1();
        ModerationAction moderationAction2 = new ModerationAction();
        assertThat(moderationAction1).isNotEqualTo(moderationAction2);

        moderationAction2.setId(moderationAction1.getId());
        assertThat(moderationAction1).isEqualTo(moderationAction2);

        moderationAction2 = getModerationActionSample2();
        assertThat(moderationAction1).isNotEqualTo(moderationAction2);
    }

    @Test
    void assistanceTicketTest() throws Exception {
        ModerationAction moderationAction = getModerationActionRandomSampleGenerator();
        AssistanceTicket assistanceTicketBack = getAssistanceTicketRandomSampleGenerator();

        moderationAction.setAssistanceTicket(assistanceTicketBack);
        assertThat(moderationAction.getAssistanceTicket()).isEqualTo(assistanceTicketBack);
        assertThat(assistanceTicketBack.getModerationAction()).isEqualTo(moderationAction);

        moderationAction.assistanceTicket(null);
        assertThat(moderationAction.getAssistanceTicket()).isNull();
        assertThat(assistanceTicketBack.getModerationAction()).isNull();
    }
}
