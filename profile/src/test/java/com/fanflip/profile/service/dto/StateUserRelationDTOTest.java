package com.monsterdam.profile.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.profile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StateUserRelationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StateUserRelationDTO.class);
        StateUserRelationDTO stateUserRelationDTO1 = new StateUserRelationDTO();
        stateUserRelationDTO1.setId(1L);
        StateUserRelationDTO stateUserRelationDTO2 = new StateUserRelationDTO();
        assertThat(stateUserRelationDTO1).isNotEqualTo(stateUserRelationDTO2);
        stateUserRelationDTO2.setId(stateUserRelationDTO1.getId());
        assertThat(stateUserRelationDTO1).isEqualTo(stateUserRelationDTO2);
        stateUserRelationDTO2.setId(2L);
        assertThat(stateUserRelationDTO1).isNotEqualTo(stateUserRelationDTO2);
        stateUserRelationDTO1.setId(null);
        assertThat(stateUserRelationDTO1).isNotEqualTo(stateUserRelationDTO2);
    }
}
