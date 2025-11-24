package com.monsterdam.interactions.domain;

import static com.monsterdam.interactions.domain.PostCommentTestSamples.*;
import static com.monsterdam.interactions.domain.PostFeedTestSamples.*;
import static com.monsterdam.interactions.domain.PostPollTestSamples.*;
import static com.monsterdam.interactions.domain.UserMentionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.interactions.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PostFeedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostFeed.class);
        PostFeed postFeed1 = getPostFeedSample1();
        PostFeed postFeed2 = new PostFeed();
        assertThat(postFeed1).isNotEqualTo(postFeed2);

        postFeed2.setId(postFeed1.getId());
        assertThat(postFeed1).isEqualTo(postFeed2);

        postFeed2 = getPostFeedSample2();
        assertThat(postFeed1).isNotEqualTo(postFeed2);
    }

    @Test
    void pollTest() throws Exception {
        PostFeed postFeed = getPostFeedRandomSampleGenerator();
        PostPoll postPollBack = getPostPollRandomSampleGenerator();

        postFeed.setPoll(postPollBack);
        assertThat(postFeed.getPoll()).isEqualTo(postPollBack);

        postFeed.poll(null);
        assertThat(postFeed.getPoll()).isNull();
    }

    @Test
    void commentsTest() throws Exception {
        PostFeed postFeed = getPostFeedRandomSampleGenerator();
        PostComment postCommentBack = getPostCommentRandomSampleGenerator();

        postFeed.addComments(postCommentBack);
        assertThat(postFeed.getComments()).containsOnly(postCommentBack);
        assertThat(postCommentBack.getPost()).isEqualTo(postFeed);

        postFeed.removeComments(postCommentBack);
        assertThat(postFeed.getComments()).doesNotContain(postCommentBack);
        assertThat(postCommentBack.getPost()).isNull();

        postFeed.comments(new HashSet<>(Set.of(postCommentBack)));
        assertThat(postFeed.getComments()).containsOnly(postCommentBack);
        assertThat(postCommentBack.getPost()).isEqualTo(postFeed);

        postFeed.setComments(new HashSet<>());
        assertThat(postFeed.getComments()).doesNotContain(postCommentBack);
        assertThat(postCommentBack.getPost()).isNull();
    }

    @Test
    void commentMentionsTest() throws Exception {
        PostFeed postFeed = getPostFeedRandomSampleGenerator();
        UserMention userMentionBack = getUserMentionRandomSampleGenerator();

        postFeed.addCommentMentions(userMentionBack);
        assertThat(postFeed.getCommentMentions()).containsOnly(userMentionBack);
        assertThat(userMentionBack.getOriginPost()).isEqualTo(postFeed);

        postFeed.removeCommentMentions(userMentionBack);
        assertThat(postFeed.getCommentMentions()).doesNotContain(userMentionBack);
        assertThat(userMentionBack.getOriginPost()).isNull();

        postFeed.commentMentions(new HashSet<>(Set.of(userMentionBack)));
        assertThat(postFeed.getCommentMentions()).containsOnly(userMentionBack);
        assertThat(userMentionBack.getOriginPost()).isEqualTo(postFeed);

        postFeed.setCommentMentions(new HashSet<>());
        assertThat(postFeed.getCommentMentions()).doesNotContain(userMentionBack);
        assertThat(userMentionBack.getOriginPost()).isNull();
    }
}
