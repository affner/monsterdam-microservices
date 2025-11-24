package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentReviewObservationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentReviewObservationDTO.class);
        DocumentReviewObservationDTO documentReviewObservationDTO1 = new DocumentReviewObservationDTO();
        documentReviewObservationDTO1.setId(1L);
        DocumentReviewObservationDTO documentReviewObservationDTO2 = new DocumentReviewObservationDTO();
        assertThat(documentReviewObservationDTO1).isNotEqualTo(documentReviewObservationDTO2);
        documentReviewObservationDTO2.setId(documentReviewObservationDTO1.getId());
        assertThat(documentReviewObservationDTO1).isEqualTo(documentReviewObservationDTO2);
        documentReviewObservationDTO2.setId(2L);
        assertThat(documentReviewObservationDTO1).isNotEqualTo(documentReviewObservationDTO2);
        documentReviewObservationDTO1.setId(null);
        assertThat(documentReviewObservationDTO1).isNotEqualTo(documentReviewObservationDTO2);
    }
}
