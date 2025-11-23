package com.fanflip.multimedia.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.multimedia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VideoStoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VideoStoryDTO.class);
        VideoStoryDTO videoStoryDTO1 = new VideoStoryDTO();
        videoStoryDTO1.setId(1L);
        VideoStoryDTO videoStoryDTO2 = new VideoStoryDTO();
        assertThat(videoStoryDTO1).isNotEqualTo(videoStoryDTO2);
        videoStoryDTO2.setId(videoStoryDTO1.getId());
        assertThat(videoStoryDTO1).isEqualTo(videoStoryDTO2);
        videoStoryDTO2.setId(2L);
        assertThat(videoStoryDTO1).isNotEqualTo(videoStoryDTO2);
        videoStoryDTO1.setId(null);
        assertThat(videoStoryDTO1).isNotEqualTo(videoStoryDTO2);
    }
}
