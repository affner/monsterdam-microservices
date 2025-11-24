package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SingleLiveStreamDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleLiveStreamDTO.class);
        SingleLiveStreamDTO singleLiveStreamDTO1 = new SingleLiveStreamDTO();
        singleLiveStreamDTO1.setId(1L);
        SingleLiveStreamDTO singleLiveStreamDTO2 = new SingleLiveStreamDTO();
        assertThat(singleLiveStreamDTO1).isNotEqualTo(singleLiveStreamDTO2);
        singleLiveStreamDTO2.setId(singleLiveStreamDTO1.getId());
        assertThat(singleLiveStreamDTO1).isEqualTo(singleLiveStreamDTO2);
        singleLiveStreamDTO2.setId(2L);
        assertThat(singleLiveStreamDTO1).isNotEqualTo(singleLiveStreamDTO2);
        singleLiveStreamDTO1.setId(null);
        assertThat(singleLiveStreamDTO1).isNotEqualTo(singleLiveStreamDTO2);
    }
}
