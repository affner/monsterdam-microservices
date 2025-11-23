package com.fanflip.interactions.domain;

import static com.fanflip.interactions.domain.PostCommentTestSamples.*;
import static com.fanflip.interactions.domain.PostFeedTestSamples.*;
import static com.fanflip.interactions.domain.UserMentionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.interactions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserMentionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMention.class);
        UserMention userMention1 = getUserMentionSample1();
        UserMention userMention2 = new UserMention();
        assertThat(userMention1).isNotEqualTo(userMention2);

        userMention2.setId(userMention1.getId());
        assertThat(userMention1).isEqualTo(userMention2);

        userMention2 = getUserMentionSample2();
        assertThat(userMention1).isNotEqualTo(userMention2);
    }

    @Test
    void originPostTest() throws Exception {
        UserMention userMention = getUserMentionRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        userMention.setOriginPost(postFeedBack);
        assertThat(userMention.getOriginPost()).isEqualTo(postFeedBack);

        userMention.originPost(null);
        assertThat(userMention.getOriginPost()).isNull();
    }

    @Test
    void originPostCommentTest() throws Exception {
        UserMention userMention = getUserMentionRandomSampleGenerator();
        PostComment postCommentBack = getPostCommentRandomSampleGenerator();

        userMention.setOriginPostComment(postCommentBack);
        assertThat(userMention.getOriginPostComment()).isEqualTo(postCommentBack);

        userMention.originPostComment(null);
        assertThat(userMention.getOriginPostComment()).isNull();
    }
}
