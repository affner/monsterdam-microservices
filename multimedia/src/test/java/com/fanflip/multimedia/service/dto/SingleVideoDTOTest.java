package com.fanflip.multimedia.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.multimedia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SingleVideoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleVideoDTO.class);
        SingleVideoDTO singleVideoDTO1 = new SingleVideoDTO();
        singleVideoDTO1.setId(1L);
        SingleVideoDTO singleVideoDTO2 = new SingleVideoDTO();
        assertThat(singleVideoDTO1).isNotEqualTo(singleVideoDTO2);
        singleVideoDTO2.setId(singleVideoDTO1.getId());
        assertThat(singleVideoDTO1).isEqualTo(singleVideoDTO2);
        singleVideoDTO2.setId(2L);
        assertThat(singleVideoDTO1).isNotEqualTo(singleVideoDTO2);
        singleVideoDTO1.setId(null);
        assertThat(singleVideoDTO1).isNotEqualTo(singleVideoDTO2);
    }
}
