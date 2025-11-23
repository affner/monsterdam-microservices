package com.fanflip.interactions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.interactions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostPollDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostPollDTO.class);
        PostPollDTO postPollDTO1 = new PostPollDTO();
        postPollDTO1.setId(1L);
        PostPollDTO postPollDTO2 = new PostPollDTO();
        assertThat(postPollDTO1).isNotEqualTo(postPollDTO2);
        postPollDTO2.setId(postPollDTO1.getId());
        assertThat(postPollDTO1).isEqualTo(postPollDTO2);
        postPollDTO2.setId(2L);
        assertThat(postPollDTO1).isNotEqualTo(postPollDTO2);
        postPollDTO1.setId(null);
        assertThat(postPollDTO1).isNotEqualTo(postPollDTO2);
    }
}
