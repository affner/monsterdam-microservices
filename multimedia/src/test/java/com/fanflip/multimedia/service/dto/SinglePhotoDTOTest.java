package com.monsterdam.multimedia.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.multimedia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SinglePhotoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SinglePhotoDTO.class);
        SinglePhotoDTO singlePhotoDTO1 = new SinglePhotoDTO();
        singlePhotoDTO1.setId(1L);
        SinglePhotoDTO singlePhotoDTO2 = new SinglePhotoDTO();
        assertThat(singlePhotoDTO1).isNotEqualTo(singlePhotoDTO2);
        singlePhotoDTO2.setId(singlePhotoDTO1.getId());
        assertThat(singlePhotoDTO1).isEqualTo(singlePhotoDTO2);
        singlePhotoDTO2.setId(2L);
        assertThat(singlePhotoDTO1).isNotEqualTo(singlePhotoDTO2);
        singlePhotoDTO1.setId(null);
        assertThat(singlePhotoDTO1).isNotEqualTo(singlePhotoDTO2);
    }
}
