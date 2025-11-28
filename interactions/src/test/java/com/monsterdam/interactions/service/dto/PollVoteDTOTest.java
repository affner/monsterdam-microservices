package com.monsterdam.interactions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.interactions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PollVoteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PollVoteDTO.class);
        PollVoteDTO pollVoteDTO1 = new PollVoteDTO();
        pollVoteDTO1.setId(1L);
        PollVoteDTO pollVoteDTO2 = new PollVoteDTO();
        assertThat(pollVoteDTO1).isNotEqualTo(pollVoteDTO2);
        pollVoteDTO2.setId(pollVoteDTO1.getId());
        assertThat(pollVoteDTO1).isEqualTo(pollVoteDTO2);
        pollVoteDTO2.setId(2L);
        assertThat(pollVoteDTO1).isNotEqualTo(pollVoteDTO2);
        pollVoteDTO1.setId(null);
        assertThat(pollVoteDTO1).isNotEqualTo(pollVoteDTO2);
    }
}
