package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssistanceTicketDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssistanceTicketDTO.class);
        AssistanceTicketDTO assistanceTicketDTO1 = new AssistanceTicketDTO();
        assistanceTicketDTO1.setId(1L);
        AssistanceTicketDTO assistanceTicketDTO2 = new AssistanceTicketDTO();
        assertThat(assistanceTicketDTO1).isNotEqualTo(assistanceTicketDTO2);
        assistanceTicketDTO2.setId(assistanceTicketDTO1.getId());
        assertThat(assistanceTicketDTO1).isEqualTo(assistanceTicketDTO2);
        assistanceTicketDTO2.setId(2L);
        assertThat(assistanceTicketDTO1).isNotEqualTo(assistanceTicketDTO2);
        assistanceTicketDTO1.setId(null);
        assertThat(assistanceTicketDTO1).isNotEqualTo(assistanceTicketDTO2);
    }
}
