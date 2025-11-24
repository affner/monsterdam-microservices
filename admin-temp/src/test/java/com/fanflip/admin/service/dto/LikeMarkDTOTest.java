package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeMarkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikeMarkDTO.class);
        LikeMarkDTO likeMarkDTO1 = new LikeMarkDTO();
        likeMarkDTO1.setId(1L);
        LikeMarkDTO likeMarkDTO2 = new LikeMarkDTO();
        assertThat(likeMarkDTO1).isNotEqualTo(likeMarkDTO2);
        likeMarkDTO2.setId(likeMarkDTO1.getId());
        assertThat(likeMarkDTO1).isEqualTo(likeMarkDTO2);
        likeMarkDTO2.setId(2L);
        assertThat(likeMarkDTO1).isNotEqualTo(likeMarkDTO2);
        likeMarkDTO1.setId(null);
        assertThat(likeMarkDTO1).isNotEqualTo(likeMarkDTO2);
    }
}
