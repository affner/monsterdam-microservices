package com.fanflip.finance.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.finance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OfferPromotionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OfferPromotionDTO.class);
        OfferPromotionDTO offerPromotionDTO1 = new OfferPromotionDTO();
        offerPromotionDTO1.setId(1L);
        OfferPromotionDTO offerPromotionDTO2 = new OfferPromotionDTO();
        assertThat(offerPromotionDTO1).isNotEqualTo(offerPromotionDTO2);
        offerPromotionDTO2.setId(offerPromotionDTO1.getId());
        assertThat(offerPromotionDTO1).isEqualTo(offerPromotionDTO2);
        offerPromotionDTO2.setId(2L);
        assertThat(offerPromotionDTO1).isNotEqualTo(offerPromotionDTO2);
        offerPromotionDTO1.setId(null);
        assertThat(offerPromotionDTO1).isNotEqualTo(offerPromotionDTO2);
    }
}
