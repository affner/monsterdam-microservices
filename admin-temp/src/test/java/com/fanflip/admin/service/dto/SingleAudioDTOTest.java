package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SingleAudioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleAudioDTO.class);
        SingleAudioDTO singleAudioDTO1 = new SingleAudioDTO();
        singleAudioDTO1.setId(1L);
        SingleAudioDTO singleAudioDTO2 = new SingleAudioDTO();
        assertThat(singleAudioDTO1).isNotEqualTo(singleAudioDTO2);
        singleAudioDTO2.setId(singleAudioDTO1.getId());
        assertThat(singleAudioDTO1).isEqualTo(singleAudioDTO2);
        singleAudioDTO2.setId(2L);
        assertThat(singleAudioDTO1).isNotEqualTo(singleAudioDTO2);
        singleAudioDTO1.setId(null);
        assertThat(singleAudioDTO1).isNotEqualTo(singleAudioDTO2);
    }
}
