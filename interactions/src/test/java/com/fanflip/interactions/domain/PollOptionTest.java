package com.monsterdam.interactions.domain;

import static com.monsterdam.interactions.domain.PollOptionTestSamples.*;
import static com.monsterdam.interactions.domain.PollVoteTestSamples.*;
import static com.monsterdam.interactions.domain.PostPollTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.interactions.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PollOptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PollOption.class);
        PollOption pollOption1 = getPollOptionSample1();
        PollOption pollOption2 = new PollOption();
        assertThat(pollOption1).isNotEqualTo(pollOption2);

        pollOption2.setId(pollOption1.getId());
        assertThat(pollOption1).isEqualTo(pollOption2);

        pollOption2 = getPollOptionSample2();
        assertThat(pollOption1).isNotEqualTo(pollOption2);
    }

    @Test
    void pollTest() throws Exception {
        PollOption pollOption = getPollOptionRandomSampleGenerator();
        PostPoll postPollBack = getPostPollRandomSampleGenerator();

        pollOption.setPoll(postPollBack);
        assertThat(pollOption.getPoll()).isEqualTo(postPollBack);

        pollOption.poll(null);
        assertThat(pollOption.getPoll()).isNull();
    }

    @Test
    void votesTest() throws Exception {
        PollOption pollOption = getPollOptionRandomSampleGenerator();
        PollVote pollVoteBack = getPollVoteRandomSampleGenerator();

        pollOption.addVotes(pollVoteBack);
        assertThat(pollOption.getVotes()).containsOnly(pollVoteBack);
        assertThat(pollVoteBack.getPollOption()).isEqualTo(pollOption);

        pollOption.removeVotes(pollVoteBack);
        assertThat(pollOption.getVotes()).doesNotContain(pollVoteBack);
        assertThat(pollVoteBack.getPollOption()).isNull();

        pollOption.votes(new HashSet<>(Set.of(pollVoteBack)));
        assertThat(pollOption.getVotes()).containsOnly(pollVoteBack);
        assertThat(pollVoteBack.getPollOption()).isEqualTo(pollOption);

        pollOption.setVotes(new HashSet<>());
        assertThat(pollOption.getVotes()).doesNotContain(pollVoteBack);
        assertThat(pollVoteBack.getPollOption()).isNull();
    }
}
