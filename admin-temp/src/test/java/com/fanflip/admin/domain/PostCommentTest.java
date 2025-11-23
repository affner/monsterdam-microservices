package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.PostCommentTestSamples.*;
import static com.fanflip.admin.domain.PostCommentTestSamples.*;
import static com.fanflip.admin.domain.PostFeedTestSamples.*;
import static com.fanflip.admin.domain.UserMentionTestSamples.*;
import static com.fanflip.admin.domain.UserProfileTestSamples.*;
import static com.fanflip.admin.domain.UserReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
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
    void reportsTest() throws Exception {
        PostComment postComment = getPostCommentRandomSampleGenerator();
        UserReport userReportBack = getUserReportRandomSampleGenerator();

        postComment.addReports(userReportBack);
        assertThat(postComment.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getPostComment()).isEqualTo(postComment);

        postComment.removeReports(userReportBack);
        assertThat(postComment.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getPostComment()).isNull();

        postComment.reports(new HashSet<>(Set.of(userReportBack)));
        assertThat(postComment.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getPostComment()).isEqualTo(postComment);

        postComment.setReports(new HashSet<>());
        assertThat(postComment.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getPostComment()).isNull();
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

    @Test
    void commenterTest() throws Exception {
        PostComment postComment = getPostCommentRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        postComment.setCommenter(userProfileBack);
        assertThat(postComment.getCommenter()).isEqualTo(userProfileBack);

        postComment.commenter(null);
        assertThat(postComment.getCommenter()).isNull();
    }
}
