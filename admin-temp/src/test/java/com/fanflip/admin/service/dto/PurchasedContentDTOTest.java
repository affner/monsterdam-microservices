package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchasedContentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasedContentDTO.class);
        PurchasedContentDTO purchasedContentDTO1 = new PurchasedContentDTO();
        purchasedContentDTO1.setId(1L);
        PurchasedContentDTO purchasedContentDTO2 = new PurchasedContentDTO();
        assertThat(purchasedContentDTO1).isNotEqualTo(purchasedContentDTO2);
        purchasedContentDTO2.setId(purchasedContentDTO1.getId());
        assertThat(purchasedContentDTO1).isEqualTo(purchasedContentDTO2);
        purchasedContentDTO2.setId(2L);
        assertThat(purchasedContentDTO1).isNotEqualTo(purchasedContentDTO2);
        purchasedContentDTO1.setId(null);
        assertThat(purchasedContentDTO1).isNotEqualTo(purchasedContentDTO2);
    }
}
