package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.AdminAnnouncementTestSamples.*;
import static com.monsterdam.admin.domain.BookMarkTestSamples.*;
import static com.monsterdam.admin.domain.ChatRoomTestSamples.*;
import static com.monsterdam.admin.domain.ContentPackageTestSamples.*;
import static com.monsterdam.admin.domain.DirectMessageTestSamples.*;
import static com.monsterdam.admin.domain.DirectMessageTestSamples.*;
import static com.monsterdam.admin.domain.PurchasedTipTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static com.monsterdam.admin.domain.UserReportTestSamples.*;
import static com.monsterdam.admin.domain.VideoStoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DirectMessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DirectMessage.class);
        DirectMessage directMessage1 = getDirectMessageSample1();
        DirectMessage directMessage2 = new DirectMessage();
        assertThat(directMessage1).isNotEqualTo(directMessage2);

        directMessage2.setId(directMessage1.getId());
        assertThat(directMessage1).isEqualTo(directMessage2);

        directMessage2 = getDirectMessageSample2();
        assertThat(directMessage1).isNotEqualTo(directMessage2);
    }

    @Test
    void contentPackageTest() throws Exception {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        directMessage.setContentPackage(contentPackageBack);
        assertThat(directMessage.getContentPackage()).isEqualTo(contentPackageBack);

        directMessage.contentPackage(null);
        assertThat(directMessage.getContentPackage()).isNull();
    }

    @Test
    void reportsTest() throws Exception {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        UserReport userReportBack = getUserReportRandomSampleGenerator();

        directMessage.addReports(userReportBack);
        assertThat(directMessage.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getMessage()).isEqualTo(directMessage);

        directMessage.removeReports(userReportBack);
        assertThat(directMessage.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getMessage()).isNull();

        directMessage.reports(new HashSet<>(Set.of(userReportBack)));
        assertThat(directMessage.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getMessage()).isEqualTo(directMessage);

        directMessage.setReports(new HashSet<>());
        assertThat(directMessage.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getMessage()).isNull();
    }

    @Test
    void responsesTest() throws Exception {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        directMessage.addResponses(directMessageBack);
        assertThat(directMessage.getResponses()).containsOnly(directMessageBack);
        assertThat(directMessageBack.getResponseTo()).isEqualTo(directMessage);

        directMessage.removeResponses(directMessageBack);
        assertThat(directMessage.getResponses()).doesNotContain(directMessageBack);
        assertThat(directMessageBack.getResponseTo()).isNull();

        directMessage.responses(new HashSet<>(Set.of(directMessageBack)));
        assertThat(directMessage.getResponses()).containsOnly(directMessageBack);
        assertThat(directMessageBack.getResponseTo()).isEqualTo(directMessage);

        directMessage.setResponses(new HashSet<>());
        assertThat(directMessage.getResponses()).doesNotContain(directMessageBack);
        assertThat(directMessageBack.getResponseTo()).isNull();
    }

    @Test
    void responseToTest() throws Exception {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        directMessage.setResponseTo(directMessageBack);
        assertThat(directMessage.getResponseTo()).isEqualTo(directMessageBack);

        directMessage.responseTo(null);
        assertThat(directMessage.getResponseTo()).isNull();
    }

    @Test
    void repliedStoryTest() throws Exception {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        VideoStory videoStoryBack = getVideoStoryRandomSampleGenerator();

        directMessage.setRepliedStory(videoStoryBack);
        assertThat(directMessage.getRepliedStory()).isEqualTo(videoStoryBack);

        directMessage.repliedStory(null);
        assertThat(directMessage.getRepliedStory()).isNull();
    }

    @Test
    void userTest() throws Exception {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        directMessage.setUser(userProfileBack);
        assertThat(directMessage.getUser()).isEqualTo(userProfileBack);

        directMessage.user(null);
        assertThat(directMessage.getUser()).isNull();
    }

    @Test
    void bookMarksTest() throws Exception {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        BookMark bookMarkBack = getBookMarkRandomSampleGenerator();

        directMessage.addBookMarks(bookMarkBack);
        assertThat(directMessage.getBookMarks()).containsOnly(bookMarkBack);
        assertThat(bookMarkBack.getMessage()).isEqualTo(directMessage);

        directMessage.removeBookMarks(bookMarkBack);
        assertThat(directMessage.getBookMarks()).doesNotContain(bookMarkBack);
        assertThat(bookMarkBack.getMessage()).isNull();

        directMessage.bookMarks(new HashSet<>(Set.of(bookMarkBack)));
        assertThat(directMessage.getBookMarks()).containsOnly(bookMarkBack);
        assertThat(bookMarkBack.getMessage()).isEqualTo(directMessage);

        directMessage.setBookMarks(new HashSet<>());
        assertThat(directMessage.getBookMarks()).doesNotContain(bookMarkBack);
        assertThat(bookMarkBack.getMessage()).isNull();
    }

    @Test
    void chatRoomsTest() throws Exception {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        ChatRoom chatRoomBack = getChatRoomRandomSampleGenerator();

        directMessage.addChatRooms(chatRoomBack);
        assertThat(directMessage.getChatRooms()).containsOnly(chatRoomBack);
        assertThat(chatRoomBack.getSentMessages()).containsOnly(directMessage);

        directMessage.removeChatRooms(chatRoomBack);
        assertThat(directMessage.getChatRooms()).doesNotContain(chatRoomBack);
        assertThat(chatRoomBack.getSentMessages()).doesNotContain(directMessage);

        directMessage.chatRooms(new HashSet<>(Set.of(chatRoomBack)));
        assertThat(directMessage.getChatRooms()).containsOnly(chatRoomBack);
        assertThat(chatRoomBack.getSentMessages()).containsOnly(directMessage);

        directMessage.setChatRooms(new HashSet<>());
        assertThat(directMessage.getChatRooms()).doesNotContain(chatRoomBack);
        assertThat(chatRoomBack.getSentMessages()).doesNotContain(directMessage);
    }

    @Test
    void adminAnnouncementTest() throws Exception {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        AdminAnnouncement adminAnnouncementBack = getAdminAnnouncementRandomSampleGenerator();

        directMessage.setAdminAnnouncement(adminAnnouncementBack);
        assertThat(directMessage.getAdminAnnouncement()).isEqualTo(adminAnnouncementBack);
        assertThat(adminAnnouncementBack.getAnnouncerMessage()).isEqualTo(directMessage);

        directMessage.adminAnnouncement(null);
        assertThat(directMessage.getAdminAnnouncement()).isNull();
        assertThat(adminAnnouncementBack.getAnnouncerMessage()).isNull();
    }

    @Test
    void purchasedTipTest() throws Exception {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        PurchasedTip purchasedTipBack = getPurchasedTipRandomSampleGenerator();

        directMessage.setPurchasedTip(purchasedTipBack);
        assertThat(directMessage.getPurchasedTip()).isEqualTo(purchasedTipBack);
        assertThat(purchasedTipBack.getMessage()).isEqualTo(directMessage);

        directMessage.purchasedTip(null);
        assertThat(directMessage.getPurchasedTip()).isNull();
        assertThat(purchasedTipBack.getMessage()).isNull();
    }
}
