package com.monsterdam.profile.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.profile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostFeedHashTagRelationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostFeedHashTagRelationDTO.class);
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO1 = new PostFeedHashTagRelationDTO();
        postFeedHashTagRelationDTO1.setId(1L);
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO2 = new PostFeedHashTagRelationDTO();
        assertThat(postFeedHashTagRelationDTO1).isNotEqualTo(postFeedHashTagRelationDTO2);
        postFeedHashTagRelationDTO2.setId(postFeedHashTagRelationDTO1.getId());
        assertThat(postFeedHashTagRelationDTO1).isEqualTo(postFeedHashTagRelationDTO2);
        postFeedHashTagRelationDTO2.setId(2L);
        assertThat(postFeedHashTagRelationDTO1).isNotEqualTo(postFeedHashTagRelationDTO2);
        postFeedHashTagRelationDTO1.setId(null);
        assertThat(postFeedHashTagRelationDTO1).isNotEqualTo(postFeedHashTagRelationDTO2);
    }
}
