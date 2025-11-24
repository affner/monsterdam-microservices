package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.PollOptionTestSamples.*;
import static com.monsterdam.admin.domain.PollVoteTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
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

    @Test
    void votingUserTest() throws Exception {
        PollVote pollVote = getPollVoteRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        pollVote.setVotingUser(userProfileBack);
        assertThat(pollVote.getVotingUser()).isEqualTo(userProfileBack);

        pollVote.votingUser(null);
        assertThat(pollVote.getVotingUser()).isNull();
    }
}
