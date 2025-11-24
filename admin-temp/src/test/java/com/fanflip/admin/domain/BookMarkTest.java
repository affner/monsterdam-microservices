package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.BookMarkTestSamples.*;
import static com.monsterdam.admin.domain.DirectMessageTestSamples.*;
import static com.monsterdam.admin.domain.PostFeedTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookMarkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookMark.class);
        BookMark bookMark1 = getBookMarkSample1();
        BookMark bookMark2 = new BookMark();
        assertThat(bookMark1).isNotEqualTo(bookMark2);

        bookMark2.setId(bookMark1.getId());
        assertThat(bookMark1).isEqualTo(bookMark2);

        bookMark2 = getBookMarkSample2();
        assertThat(bookMark1).isNotEqualTo(bookMark2);
    }

    @Test
    void postTest() throws Exception {
        BookMark bookMark = getBookMarkRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        bookMark.setPost(postFeedBack);
        assertThat(bookMark.getPost()).isEqualTo(postFeedBack);

        bookMark.post(null);
        assertThat(bookMark.getPost()).isNull();
    }

    @Test
    void messageTest() throws Exception {
        BookMark bookMark = getBookMarkRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        bookMark.setMessage(directMessageBack);
        assertThat(bookMark.getMessage()).isEqualTo(directMessageBack);

        bookMark.message(null);
        assertThat(bookMark.getMessage()).isNull();
    }

    @Test
    void userTest() throws Exception {
        BookMark bookMark = getBookMarkRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        bookMark.setUser(userProfileBack);
        assertThat(bookMark.getUser()).isEqualTo(userProfileBack);

        bookMark.user(null);
        assertThat(bookMark.getUser()).isNull();
    }
}
