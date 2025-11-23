package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyPayoutDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyPayoutDTO.class);
        MoneyPayoutDTO moneyPayoutDTO1 = new MoneyPayoutDTO();
        moneyPayoutDTO1.setId(1L);
        MoneyPayoutDTO moneyPayoutDTO2 = new MoneyPayoutDTO();
        assertThat(moneyPayoutDTO1).isNotEqualTo(moneyPayoutDTO2);
        moneyPayoutDTO2.setId(moneyPayoutDTO1.getId());
        assertThat(moneyPayoutDTO1).isEqualTo(moneyPayoutDTO2);
        moneyPayoutDTO2.setId(2L);
        assertThat(moneyPayoutDTO1).isNotEqualTo(moneyPayoutDTO2);
        moneyPayoutDTO1.setId(null);
        assertThat(moneyPayoutDTO1).isNotEqualTo(moneyPayoutDTO2);
    }
}
