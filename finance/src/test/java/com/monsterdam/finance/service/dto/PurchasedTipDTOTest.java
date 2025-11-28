package com.monsterdam.finance.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.finance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchasedTipDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasedTipDTO.class);
        PurchasedTipDTO purchasedTipDTO1 = new PurchasedTipDTO();
        purchasedTipDTO1.setId(1L);
        PurchasedTipDTO purchasedTipDTO2 = new PurchasedTipDTO();
        assertThat(purchasedTipDTO1).isNotEqualTo(purchasedTipDTO2);
        purchasedTipDTO2.setId(purchasedTipDTO1.getId());
        assertThat(purchasedTipDTO1).isEqualTo(purchasedTipDTO2);
        purchasedTipDTO2.setId(2L);
        assertThat(purchasedTipDTO1).isNotEqualTo(purchasedTipDTO2);
        purchasedTipDTO1.setId(null);
        assertThat(purchasedTipDTO1).isNotEqualTo(purchasedTipDTO2);
    }
}
