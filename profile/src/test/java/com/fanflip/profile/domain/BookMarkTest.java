package com.monsterdam.profile.domain;

import static com.monsterdam.profile.domain.BookMarkTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.profile.web.rest.TestUtil;
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
}
