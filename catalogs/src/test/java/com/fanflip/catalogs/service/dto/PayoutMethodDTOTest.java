package com.monsterdam.catalogs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.catalogs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PayoutMethodDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayoutMethodDTO.class);
        PayoutMethodDTO payoutMethodDTO1 = new PayoutMethodDTO();
        payoutMethodDTO1.setId(1L);
        PayoutMethodDTO payoutMethodDTO2 = new PayoutMethodDTO();
        assertThat(payoutMethodDTO1).isNotEqualTo(payoutMethodDTO2);
        payoutMethodDTO2.setId(payoutMethodDTO1.getId());
        assertThat(payoutMethodDTO1).isEqualTo(payoutMethodDTO2);
        payoutMethodDTO2.setId(2L);
        assertThat(payoutMethodDTO1).isNotEqualTo(payoutMethodDTO2);
        payoutMethodDTO1.setId(null);
        assertThat(payoutMethodDTO1).isNotEqualTo(payoutMethodDTO2);
    }
}
