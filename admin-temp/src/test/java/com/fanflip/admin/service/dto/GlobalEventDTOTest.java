package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GlobalEventDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlobalEventDTO.class);
        GlobalEventDTO globalEventDTO1 = new GlobalEventDTO();
        globalEventDTO1.setId(1L);
        GlobalEventDTO globalEventDTO2 = new GlobalEventDTO();
        assertThat(globalEventDTO1).isNotEqualTo(globalEventDTO2);
        globalEventDTO2.setId(globalEventDTO1.getId());
        assertThat(globalEventDTO1).isEqualTo(globalEventDTO2);
        globalEventDTO2.setId(2L);
        assertThat(globalEventDTO1).isNotEqualTo(globalEventDTO2);
        globalEventDTO1.setId(null);
        assertThat(globalEventDTO1).isNotEqualTo(globalEventDTO2);
    }
}
