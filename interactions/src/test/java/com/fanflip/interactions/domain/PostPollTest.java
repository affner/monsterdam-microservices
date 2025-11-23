package com.fanflip.interactions.domain;

import static com.fanflip.interactions.domain.PollOptionTestSamples.*;
import static com.fanflip.interactions.domain.PostFeedTestSamples.*;
import static com.fanflip.interactions.domain.PostPollTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.interactions.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PostPollTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostPoll.class);
        PostPoll postPoll1 = getPostPollSample1();
        PostPoll postPoll2 = new PostPoll();
        assertThat(postPoll1).isNotEqualTo(postPoll2);

        postPoll2.setId(postPoll1.getId());
        assertThat(postPoll1).isEqualTo(postPoll2);

        postPoll2 = getPostPollSample2();
        assertThat(postPoll1).isNotEqualTo(postPoll2);
    }

    @Test
    void optionsTest() throws Exception {
        PostPoll postPoll = getPostPollRandomSampleGenerator();
        PollOption pollOptionBack = getPollOptionRandomSampleGenerator();

        postPoll.addOptions(pollOptionBack);
        assertThat(postPoll.getOptions()).containsOnly(pollOptionBack);
        assertThat(pollOptionBack.getPoll()).isEqualTo(postPoll);

        postPoll.removeOptions(pollOptionBack);
        assertThat(postPoll.getOptions()).doesNotContain(pollOptionBack);
        assertThat(pollOptionBack.getPoll()).isNull();

        postPoll.options(new HashSet<>(Set.of(pollOptionBack)));
        assertThat(postPoll.getOptions()).containsOnly(pollOptionBack);
        assertThat(pollOptionBack.getPoll()).isEqualTo(postPoll);

        postPoll.setOptions(new HashSet<>());
        assertThat(postPoll.getOptions()).doesNotContain(pollOptionBack);
        assertThat(pollOptionBack.getPoll()).isNull();
    }

    @Test
    void postTest() throws Exception {
        PostPoll postPoll = getPostPollRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        postPoll.setPost(postFeedBack);
        assertThat(postPoll.getPost()).isEqualTo(postFeedBack);
        assertThat(postFeedBack.getPoll()).isEqualTo(postPoll);

        postPoll.post(null);
        assertThat(postPoll.getPost()).isNull();
        assertThat(postFeedBack.getPoll()).isNull();
    }
}
