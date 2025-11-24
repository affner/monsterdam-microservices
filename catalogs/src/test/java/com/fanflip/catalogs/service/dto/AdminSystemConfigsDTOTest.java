package com.monsterdam.catalogs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.catalogs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdminSystemConfigsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminSystemConfigsDTO.class);
        AdminSystemConfigsDTO adminSystemConfigsDTO1 = new AdminSystemConfigsDTO();
        adminSystemConfigsDTO1.setId(1L);
        AdminSystemConfigsDTO adminSystemConfigsDTO2 = new AdminSystemConfigsDTO();
        assertThat(adminSystemConfigsDTO1).isNotEqualTo(adminSystemConfigsDTO2);
        adminSystemConfigsDTO2.setId(adminSystemConfigsDTO1.getId());
        assertThat(adminSystemConfigsDTO1).isEqualTo(adminSystemConfigsDTO2);
        adminSystemConfigsDTO2.setId(2L);
        assertThat(adminSystemConfigsDTO1).isNotEqualTo(adminSystemConfigsDTO2);
        adminSystemConfigsDTO1.setId(null);
        assertThat(adminSystemConfigsDTO1).isNotEqualTo(adminSystemConfigsDTO2);
    }
}
