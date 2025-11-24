package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdminUserProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminUserProfileDTO.class);
        AdminUserProfileDTO adminUserProfileDTO1 = new AdminUserProfileDTO();
        adminUserProfileDTO1.setId(1L);
        AdminUserProfileDTO adminUserProfileDTO2 = new AdminUserProfileDTO();
        assertThat(adminUserProfileDTO1).isNotEqualTo(adminUserProfileDTO2);
        adminUserProfileDTO2.setId(adminUserProfileDTO1.getId());
        assertThat(adminUserProfileDTO1).isEqualTo(adminUserProfileDTO2);
        adminUserProfileDTO2.setId(2L);
        assertThat(adminUserProfileDTO1).isNotEqualTo(adminUserProfileDTO2);
        adminUserProfileDTO1.setId(null);
        assertThat(adminUserProfileDTO1).isNotEqualTo(adminUserProfileDTO2);
    }
}
