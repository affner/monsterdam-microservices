package com.fanflip.interactions.domain;

import static com.fanflip.interactions.domain.PollOptionTestSamples.*;
import static com.fanflip.interactions.domain.PollVoteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.interactions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PollVoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PollVote.class);
        PollVote pollVote1 = getPollVoteSample1();
        PollVote pollVote2 = new PollVote();
        assertThat(pollVote1).isNotEqualTo(pollVote2);

        pollVote2.setId(pollVote1.getId());
        assertThat(pollVote1).isEqualTo(pollVote2);

        pollVote2 = getPollVoteSample2();
        assertThat(pollVote1).isNotEqualTo(pollVote2);
    }

    @Test
    void pollOptionTest() throws Exception {
        PollVote pollVote = getPollVoteRandomSampleGenerator();
        PollOption pollOptionBack = getPollOptionRandomSampleGenerator();

        pollVote.setPollOption(pollOptionBack);
        assertThat(pollVote.getPollOption()).isEqualTo(pollOptionBack);

        pollVote.pollOption(null);
        assertThat(pollVote.getPollOption()).isNull();
    }
}
