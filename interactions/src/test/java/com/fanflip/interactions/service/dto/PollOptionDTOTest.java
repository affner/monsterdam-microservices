package com.fanflip.interactions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.interactions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PollOptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PollOptionDTO.class);
        PollOptionDTO pollOptionDTO1 = new PollOptionDTO();
        pollOptionDTO1.setId(1L);
        PollOptionDTO pollOptionDTO2 = new PollOptionDTO();
        assertThat(pollOptionDTO1).isNotEqualTo(pollOptionDTO2);
        pollOptionDTO2.setId(pollOptionDTO1.getId());
        assertThat(pollOptionDTO1).isEqualTo(pollOptionDTO2);
        pollOptionDTO2.setId(2L);
        assertThat(pollOptionDTO1).isNotEqualTo(pollOptionDTO2);
        pollOptionDTO1.setId(null);
        assertThat(pollOptionDTO1).isNotEqualTo(pollOptionDTO2);
    }
}
