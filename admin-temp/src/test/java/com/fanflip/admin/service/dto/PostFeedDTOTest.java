package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostFeedDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostFeedDTO.class);
        PostFeedDTO postFeedDTO1 = new PostFeedDTO();
        postFeedDTO1.setId(1L);
        PostFeedDTO postFeedDTO2 = new PostFeedDTO();
        assertThat(postFeedDTO1).isNotEqualTo(postFeedDTO2);
        postFeedDTO2.setId(postFeedDTO1.getId());
        assertThat(postFeedDTO1).isEqualTo(postFeedDTO2);
        postFeedDTO2.setId(2L);
        assertThat(postFeedDTO1).isNotEqualTo(postFeedDTO2);
        postFeedDTO1.setId(null);
        assertThat(postFeedDTO1).isNotEqualTo(postFeedDTO2);
    }
}
