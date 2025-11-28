package com.monsterdam.multimedia.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.multimedia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserTagRelationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserTagRelationDTO.class);
        UserTagRelationDTO userTagRelationDTO1 = new UserTagRelationDTO();
        userTagRelationDTO1.setId(1L);
        UserTagRelationDTO userTagRelationDTO2 = new UserTagRelationDTO();
        assertThat(userTagRelationDTO1).isNotEqualTo(userTagRelationDTO2);
        userTagRelationDTO2.setId(userTagRelationDTO1.getId());
        assertThat(userTagRelationDTO1).isEqualTo(userTagRelationDTO2);
        userTagRelationDTO2.setId(2L);
        assertThat(userTagRelationDTO1).isNotEqualTo(userTagRelationDTO2);
        userTagRelationDTO1.setId(null);
        assertThat(userTagRelationDTO1).isNotEqualTo(userTagRelationDTO2);
    }
}
