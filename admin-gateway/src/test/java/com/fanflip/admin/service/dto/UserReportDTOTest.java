package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserReportDTO.class);
        UserReportDTO userReportDTO1 = new UserReportDTO();
        userReportDTO1.setId(1L);
        UserReportDTO userReportDTO2 = new UserReportDTO();
        assertThat(userReportDTO1).isNotEqualTo(userReportDTO2);
        userReportDTO2.setId(userReportDTO1.getId());
        assertThat(userReportDTO1).isEqualTo(userReportDTO2);
        userReportDTO2.setId(2L);
        assertThat(userReportDTO1).isNotEqualTo(userReportDTO2);
        userReportDTO1.setId(null);
        assertThat(userReportDTO1).isNotEqualTo(userReportDTO2);
    }
}
