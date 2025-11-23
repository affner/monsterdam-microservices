package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdminAnnouncementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminAnnouncementDTO.class);
        AdminAnnouncementDTO adminAnnouncementDTO1 = new AdminAnnouncementDTO();
        adminAnnouncementDTO1.setId(1L);
        AdminAnnouncementDTO adminAnnouncementDTO2 = new AdminAnnouncementDTO();
        assertThat(adminAnnouncementDTO1).isNotEqualTo(adminAnnouncementDTO2);
        adminAnnouncementDTO2.setId(adminAnnouncementDTO1.getId());
        assertThat(adminAnnouncementDTO1).isEqualTo(adminAnnouncementDTO2);
        adminAnnouncementDTO2.setId(2L);
        assertThat(adminAnnouncementDTO1).isNotEqualTo(adminAnnouncementDTO2);
        adminAnnouncementDTO1.setId(null);
        assertThat(adminAnnouncementDTO1).isNotEqualTo(adminAnnouncementDTO2);
    }
}
