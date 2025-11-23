package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.AssistanceTicketTestSamples.*;
import static com.fanflip.admin.domain.DirectMessageTestSamples.*;
import static com.fanflip.admin.domain.PostCommentTestSamples.*;
import static com.fanflip.admin.domain.PostFeedTestSamples.*;
import static com.fanflip.admin.domain.SingleAudioTestSamples.*;
import static com.fanflip.admin.domain.SingleLiveStreamTestSamples.*;
import static com.fanflip.admin.domain.SinglePhotoTestSamples.*;
import static com.fanflip.admin.domain.SingleVideoTestSamples.*;
import static com.fanflip.admin.domain.UserProfileTestSamples.*;
import static com.fanflip.admin.domain.UserReportTestSamples.*;
import static com.fanflip.admin.domain.VideoStoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserReport.class);
        UserReport userReport1 = getUserReportSample1();
        UserReport userReport2 = new UserReport();
        assertThat(userReport1).isNotEqualTo(userReport2);

        userReport2.setId(userReport1.getId());
        assertThat(userReport1).isEqualTo(userReport2);

        userReport2 = getUserReportSample2();
        assertThat(userReport1).isNotEqualTo(userReport2);
    }

    @Test
    void ticketTest() throws Exception {
        UserReport userReport = getUserReportRandomSampleGenerator();
        AssistanceTicket assistanceTicketBack = getAssistanceTicketRandomSampleGenerator();

        userReport.setTicket(assistanceTicketBack);
        assertThat(userReport.getTicket()).isEqualTo(assistanceTicketBack);

        userReport.ticket(null);
        assertThat(userReport.getTicket()).isNull();
    }

    @Test
    void reporterTest() throws Exception {
        UserReport userReport = getUserReportRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userReport.setReporter(userProfileBack);
        assertThat(userReport.getReporter()).isEqualTo(userProfileBack);

        userReport.reporter(null);
        assertThat(userReport.getReporter()).isNull();
    }

    @Test
    void reportedTest() throws Exception {
        UserReport userReport = getUserReportRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userReport.setReported(userProfileBack);
        assertThat(userReport.getReported()).isEqualTo(userProfileBack);

        userReport.reported(null);
        assertThat(userReport.getReported()).isNull();
    }

    @Test
    void storyTest() throws Exception {
        UserReport userReport = getUserReportRandomSampleGenerator();
        VideoStory videoStoryBack = getVideoStoryRandomSampleGenerator();

        userReport.setStory(videoStoryBack);
        assertThat(userReport.getStory()).isEqualTo(videoStoryBack);

        userReport.story(null);
        assertThat(userReport.getStory()).isNull();
    }

    @Test
    void videoTest() throws Exception {
        UserReport userReport = getUserReportRandomSampleGenerator();
        SingleVideo singleVideoBack = getSingleVideoRandomSampleGenerator();

        userReport.setVideo(singleVideoBack);
        assertThat(userReport.getVideo()).isEqualTo(singleVideoBack);

        userReport.video(null);
        assertThat(userReport.getVideo()).isNull();
    }

    @Test
    void photoTest() throws Exception {
        UserReport userReport = getUserReportRandomSampleGenerator();
        SinglePhoto singlePhotoBack = getSinglePhotoRandomSampleGenerator();

        userReport.setPhoto(singlePhotoBack);
        assertThat(userReport.getPhoto()).isEqualTo(singlePhotoBack);

        userReport.photo(null);
        assertThat(userReport.getPhoto()).isNull();
    }

    @Test
    void audioTest() throws Exception {
        UserReport userReport = getUserReportRandomSampleGenerator();
        SingleAudio singleAudioBack = getSingleAudioRandomSampleGenerator();

        userReport.setAudio(singleAudioBack);
        assertThat(userReport.getAudio()).isEqualTo(singleAudioBack);

        userReport.audio(null);
        assertThat(userReport.getAudio()).isNull();
    }

    @Test
    void liveStreamTest() throws Exception {
        UserReport userReport = getUserReportRandomSampleGenerator();
        SingleLiveStream singleLiveStreamBack = getSingleLiveStreamRandomSampleGenerator();

        userReport.setLiveStream(singleLiveStreamBack);
        assertThat(userReport.getLiveStream()).isEqualTo(singleLiveStreamBack);

        userReport.liveStream(null);
        assertThat(userReport.getLiveStream()).isNull();
    }

    @Test
    void messageTest() throws Exception {
        UserReport userReport = getUserReportRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        userReport.setMessage(directMessageBack);
        assertThat(userReport.getMessage()).isEqualTo(directMessageBack);

        userReport.message(null);
        assertThat(userReport.getMessage()).isNull();
    }

    @Test
    void postTest() throws Exception {
        UserReport userReport = getUserReportRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        userReport.setPost(postFeedBack);
        assertThat(userReport.getPost()).isEqualTo(postFeedBack);

        userReport.post(null);
        assertThat(userReport.getPost()).isNull();
    }

    @Test
    void postCommentTest() throws Exception {
        UserReport userReport = getUserReportRandomSampleGenerator();
        PostComment postCommentBack = getPostCommentRandomSampleGenerator();

        userReport.setPostComment(postCommentBack);
        assertThat(userReport.getPostComment()).isEqualTo(postCommentBack);

        userReport.postComment(null);
        assertThat(userReport.getPostComment()).isNull();
    }
}
