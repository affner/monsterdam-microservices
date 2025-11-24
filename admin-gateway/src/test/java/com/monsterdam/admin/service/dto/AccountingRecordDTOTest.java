package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountingRecordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountingRecordDTO.class);
        AccountingRecordDTO accountingRecordDTO1 = new AccountingRecordDTO();
        accountingRecordDTO1.setId(1L);
        AccountingRecordDTO accountingRecordDTO2 = new AccountingRecordDTO();
        assertThat(accountingRecordDTO1).isNotEqualTo(accountingRecordDTO2);
        accountingRecordDTO2.setId(accountingRecordDTO1.getId());
        assertThat(accountingRecordDTO1).isEqualTo(accountingRecordDTO2);
        accountingRecordDTO2.setId(2L);
        assertThat(accountingRecordDTO1).isNotEqualTo(accountingRecordDTO2);
        accountingRecordDTO1.setId(null);
        assertThat(accountingRecordDTO1).isNotEqualTo(accountingRecordDTO2);
    }
}
