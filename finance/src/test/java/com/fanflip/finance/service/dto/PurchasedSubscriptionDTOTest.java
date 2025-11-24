package com.monsterdam.finance.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.finance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchasedSubscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasedSubscriptionDTO.class);
        PurchasedSubscriptionDTO purchasedSubscriptionDTO1 = new PurchasedSubscriptionDTO();
        purchasedSubscriptionDTO1.setId(1L);
        PurchasedSubscriptionDTO purchasedSubscriptionDTO2 = new PurchasedSubscriptionDTO();
        assertThat(purchasedSubscriptionDTO1).isNotEqualTo(purchasedSubscriptionDTO2);
        purchasedSubscriptionDTO2.setId(purchasedSubscriptionDTO1.getId());
        assertThat(purchasedSubscriptionDTO1).isEqualTo(purchasedSubscriptionDTO2);
        purchasedSubscriptionDTO2.setId(2L);
        assertThat(purchasedSubscriptionDTO1).isNotEqualTo(purchasedSubscriptionDTO2);
        purchasedSubscriptionDTO1.setId(null);
        assertThat(purchasedSubscriptionDTO1).isNotEqualTo(purchasedSubscriptionDTO2);
    }
}
