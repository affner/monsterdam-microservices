package com.fanflip.interactions.domain;

import static com.fanflip.interactions.domain.PostCommentTestSamples.*;
import static com.fanflip.interactions.domain.PostCommentTestSamples.*;
import static com.fanflip.interactions.domain.PostFeedTestSamples.*;
import static com.fanflip.interactions.domain.UserMentionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.interactions.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PostCommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostComment.class);
        PostComment postComment1 = getPostCommentSample1();
        PostComment postComment2 = new PostComment();
        assertThat(postComment1).isNotEqualTo(postComment2);

        postComment2.setId(postComment1.getId());
        assertThat(postComment1).isEqualTo(postComment2);

        postComment2 = getPostCommentSample2();
        assertThat(postComment1).isNotEqualTo(postComment2);
    }

    @Test
    void responsesTest() throws Exception {
        PostComment postComment = getPostCommentRandomSampleGenerator();
        PostComment postCommentBack = getPostCommentRandomSampleGenerator();

        postComment.addResponses(postCommentBack);
        assertThat(postComment.getResponses()).containsOnly(postCommentBack);
        assertThat(postCommentBack.getResponseTo()).isEqualTo(postComment);

        postComment.removeResponses(postCommentBack);
        assertThat(postComment.getResponses()).doesNotContain(postCommentBack);
        assertThat(postCommentBack.getResponseTo()).isNull();

        postComment.responses(new HashSet<>(Set.of(postCommentBack)));
        assertThat(postComment.getResponses()).containsOnly(postCommentBack);
        assertThat(postCommentBack.getResponseTo()).isEqualTo(postComment);

        postComment.setResponses(new HashSet<>());
        assertThat(postComment.getResponses()).doesNotContain(postCommentBack);
        assertThat(postCommentBack.getResponseTo()).isNull();
    }

    @Test
    void commentMentionsTest() throws Exception {
        PostComment postComment = getPostCommentRandomSampleGenerator();
        UserMention userMentionBack = getUserMentionRandomSampleGenerator();

        postComment.addCommentMentions(userMentionBack);
        assertThat(postComment.getCommentMentions()).containsOnly(userMentionBack);
        assertThat(userMentionBack.getOriginPostComment()).isEqualTo(postComment);

        postComment.removeCommentMentions(userMentionBack);
        assertThat(postComment.getCommentMentions()).doesNotContain(userMentionBack);
        assertThat(userMentionBack.getOriginPostComment()).isNull();

        postComment.commentMentions(new HashSet<>(Set.of(userMentionBack)));
        assertThat(postComment.getCommentMentions()).containsOnly(userMentionBack);
        assertThat(userMentionBack.getOriginPostComment()).isEqualTo(postComment);

        postComment.setCommentMentions(new HashSet<>());
        assertThat(postComment.getCommentMentions()).doesNotContain(userMentionBack);
        assertThat(userMentionBack.getOriginPostComment()).isNull();
    }

    @Test
    void postTest() throws Exception {
        PostComment postComment = getPostCommentRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        postComment.setPost(postFeedBack);
        assertThat(postComment.getPost()).isEqualTo(postFeedBack);

        postComment.post(null);
        assertThat(postComment.getPost()).isNull();
    }

    @Test
    void responseToTest() throws Exception {
        PostComment postComment = getPostCommentRandomSampleGenerator();
        PostComment postCommentBack = getPostCommentRandomSampleGenerator();

        postComment.setResponseTo(postCommentBack);
        assertThat(postComment.getResponseTo()).isEqualTo(postCommentBack);

        postComment.responseTo(null);
        assertThat(postComment.getResponseTo()).isNull();
    }
}
