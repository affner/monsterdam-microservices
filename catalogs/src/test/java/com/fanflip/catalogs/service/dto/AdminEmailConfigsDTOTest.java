package com.monsterdam.catalogs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.catalogs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdminEmailConfigsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminEmailConfigsDTO.class);
        AdminEmailConfigsDTO adminEmailConfigsDTO1 = new AdminEmailConfigsDTO();
        adminEmailConfigsDTO1.setId(1L);
        AdminEmailConfigsDTO adminEmailConfigsDTO2 = new AdminEmailConfigsDTO();
        assertThat(adminEmailConfigsDTO1).isNotEqualTo(adminEmailConfigsDTO2);
        adminEmailConfigsDTO2.setId(adminEmailConfigsDTO1.getId());
        assertThat(adminEmailConfigsDTO1).isEqualTo(adminEmailConfigsDTO2);
        adminEmailConfigsDTO2.setId(2L);
        assertThat(adminEmailConfigsDTO1).isNotEqualTo(adminEmailConfigsDTO2);
        adminEmailConfigsDTO1.setId(null);
        assertThat(adminEmailConfigsDTO1).isNotEqualTo(adminEmailConfigsDTO2);
    }
}
