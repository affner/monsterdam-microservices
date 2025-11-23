package com.fanflip.profile.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.profile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookMarkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookMarkDTO.class);
        BookMarkDTO bookMarkDTO1 = new BookMarkDTO();
        bookMarkDTO1.setId(1L);
        BookMarkDTO bookMarkDTO2 = new BookMarkDTO();
        assertThat(bookMarkDTO1).isNotEqualTo(bookMarkDTO2);
        bookMarkDTO2.setId(bookMarkDTO1.getId());
        assertThat(bookMarkDTO1).isEqualTo(bookMarkDTO2);
        bookMarkDTO2.setId(2L);
        assertThat(bookMarkDTO1).isNotEqualTo(bookMarkDTO2);
        bookMarkDTO1.setId(null);
        assertThat(bookMarkDTO1).isNotEqualTo(bookMarkDTO2);
    }
}
