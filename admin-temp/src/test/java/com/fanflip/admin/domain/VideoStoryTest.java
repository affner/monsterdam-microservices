package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.DirectMessageTestSamples.*;
import static com.fanflip.admin.domain.UserProfileTestSamples.*;
import static com.fanflip.admin.domain.UserReportTestSamples.*;
import static com.fanflip.admin.domain.VideoStoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VideoStoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VideoStory.class);
        VideoStory videoStory1 = getVideoStorySample1();
        VideoStory videoStory2 = new VideoStory();
        assertThat(videoStory1).isNotEqualTo(videoStory2);

        videoStory2.setId(videoStory1.getId());
        assertThat(videoStory1).isEqualTo(videoStory2);

        videoStory2 = getVideoStorySample2();
        assertThat(videoStory1).isNotEqualTo(videoStory2);
    }

    @Test
    void reportsTest() throws Exception {
        VideoStory videoStory = getVideoStoryRandomSampleGenerator();
        UserReport userReportBack = getUserReportRandomSampleGenerator();

        videoStory.addReports(userReportBack);
        assertThat(videoStory.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getStory()).isEqualTo(videoStory);

        videoStory.removeReports(userReportBack);
        assertThat(videoStory.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getStory()).isNull();

        videoStory.reports(new HashSet<>(Set.of(userReportBack)));
        assertThat(videoStory.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getStory()).isEqualTo(videoStory);

        videoStory.setReports(new HashSet<>());
        assertThat(videoStory.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getStory()).isNull();
    }

    @Test
    void messagesTest() throws Exception {
        VideoStory videoStory = getVideoStoryRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        videoStory.addMessages(directMessageBack);
        assertThat(videoStory.getMessages()).containsOnly(directMessageBack);
        assertThat(directMessageBack.getRepliedStory()).isEqualTo(videoStory);

        videoStory.removeMessages(directMessageBack);
        assertThat(videoStory.getMessages()).doesNotContain(directMessageBack);
        assertThat(directMessageBack.getRepliedStory()).isNull();

        videoStory.messages(new HashSet<>(Set.of(directMessageBack)));
        assertThat(videoStory.getMessages()).containsOnly(directMessageBack);
        assertThat(directMessageBack.getRepliedStory()).isEqualTo(videoStory);

        videoStory.setMessages(new HashSet<>());
        assertThat(videoStory.getMessages()).doesNotContain(directMessageBack);
        assertThat(directMessageBack.getRepliedStory()).isNull();
    }

    @Test
    void creatorTest() throws Exception {
        VideoStory videoStory = getVideoStoryRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        videoStory.setCreator(userProfileBack);
        assertThat(videoStory.getCreator()).isEqualTo(userProfileBack);

        videoStory.creator(null);
        assertThat(videoStory.getCreator()).isNull();
    }
}
