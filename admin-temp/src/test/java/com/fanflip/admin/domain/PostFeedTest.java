package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.BookMarkTestSamples.*;
import static com.monsterdam.admin.domain.ContentPackageTestSamples.*;
import static com.monsterdam.admin.domain.HashTagTestSamples.*;
import static com.monsterdam.admin.domain.PostCommentTestSamples.*;
import static com.monsterdam.admin.domain.PostFeedTestSamples.*;
import static com.monsterdam.admin.domain.PostPollTestSamples.*;
import static com.monsterdam.admin.domain.UserMentionTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static com.monsterdam.admin.domain.UserReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
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
    void contentPackageTest() throws Exception {
        PostFeed postFeed = getPostFeedRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        postFeed.setContentPackage(contentPackageBack);
        assertThat(postFeed.getContentPackage()).isEqualTo(contentPackageBack);

        postFeed.contentPackage(null);
        assertThat(postFeed.getContentPackage()).isNull();
    }

    @Test
    void reportsTest() throws Exception {
        PostFeed postFeed = getPostFeedRandomSampleGenerator();
        UserReport userReportBack = getUserReportRandomSampleGenerator();

        postFeed.addReports(userReportBack);
        assertThat(postFeed.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getPost()).isEqualTo(postFeed);

        postFeed.removeReports(userReportBack);
        assertThat(postFeed.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getPost()).isNull();

        postFeed.reports(new HashSet<>(Set.of(userReportBack)));
        assertThat(postFeed.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getPost()).isEqualTo(postFeed);

        postFeed.setReports(new HashSet<>());
        assertThat(postFeed.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getPost()).isNull();
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

    @Test
    void hashTagsTest() throws Exception {
        PostFeed postFeed = getPostFeedRandomSampleGenerator();
        HashTag hashTagBack = getHashTagRandomSampleGenerator();

        postFeed.addHashTags(hashTagBack);
        assertThat(postFeed.getHashTags()).containsOnly(hashTagBack);

        postFeed.removeHashTags(hashTagBack);
        assertThat(postFeed.getHashTags()).doesNotContain(hashTagBack);

        postFeed.hashTags(new HashSet<>(Set.of(hashTagBack)));
        assertThat(postFeed.getHashTags()).containsOnly(hashTagBack);

        postFeed.setHashTags(new HashSet<>());
        assertThat(postFeed.getHashTags()).doesNotContain(hashTagBack);
    }

    @Test
    void creatorTest() throws Exception {
        PostFeed postFeed = getPostFeedRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        postFeed.setCreator(userProfileBack);
        assertThat(postFeed.getCreator()).isEqualTo(userProfileBack);

        postFeed.creator(null);
        assertThat(postFeed.getCreator()).isNull();
    }

    @Test
    void bookMarksTest() throws Exception {
        PostFeed postFeed = getPostFeedRandomSampleGenerator();
        BookMark bookMarkBack = getBookMarkRandomSampleGenerator();

        postFeed.addBookMarks(bookMarkBack);
        assertThat(postFeed.getBookMarks()).containsOnly(bookMarkBack);
        assertThat(bookMarkBack.getPost()).isEqualTo(postFeed);

        postFeed.removeBookMarks(bookMarkBack);
        assertThat(postFeed.getBookMarks()).doesNotContain(bookMarkBack);
        assertThat(bookMarkBack.getPost()).isNull();

        postFeed.bookMarks(new HashSet<>(Set.of(bookMarkBack)));
        assertThat(postFeed.getBookMarks()).containsOnly(bookMarkBack);
        assertThat(bookMarkBack.getPost()).isEqualTo(postFeed);

        postFeed.setBookMarks(new HashSet<>());
        assertThat(postFeed.getBookMarks()).doesNotContain(bookMarkBack);
        assertThat(bookMarkBack.getPost()).isNull();
    }
}
