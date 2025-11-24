package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdentityDocumentReviewDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdentityDocumentReviewDTO.class);
        IdentityDocumentReviewDTO identityDocumentReviewDTO1 = new IdentityDocumentReviewDTO();
        identityDocumentReviewDTO1.setId(1L);
        IdentityDocumentReviewDTO identityDocumentReviewDTO2 = new IdentityDocumentReviewDTO();
        assertThat(identityDocumentReviewDTO1).isNotEqualTo(identityDocumentReviewDTO2);
        identityDocumentReviewDTO2.setId(identityDocumentReviewDTO1.getId());
        assertThat(identityDocumentReviewDTO1).isEqualTo(identityDocumentReviewDTO2);
        identityDocumentReviewDTO2.setId(2L);
        assertThat(identityDocumentReviewDTO1).isNotEqualTo(identityDocumentReviewDTO2);
        identityDocumentReviewDTO1.setId(null);
        assertThat(identityDocumentReviewDTO1).isNotEqualTo(identityDocumentReviewDTO2);
    }
}
