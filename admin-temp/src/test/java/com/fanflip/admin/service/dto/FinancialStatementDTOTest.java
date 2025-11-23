package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinancialStatementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinancialStatementDTO.class);
        FinancialStatementDTO financialStatementDTO1 = new FinancialStatementDTO();
        financialStatementDTO1.setId(1L);
        FinancialStatementDTO financialStatementDTO2 = new FinancialStatementDTO();
        assertThat(financialStatementDTO1).isNotEqualTo(financialStatementDTO2);
        financialStatementDTO2.setId(financialStatementDTO1.getId());
        assertThat(financialStatementDTO1).isEqualTo(financialStatementDTO2);
        financialStatementDTO2.setId(2L);
        assertThat(financialStatementDTO1).isNotEqualTo(financialStatementDTO2);
        financialStatementDTO1.setId(null);
        assertThat(financialStatementDTO1).isNotEqualTo(financialStatementDTO2);
    }
}
