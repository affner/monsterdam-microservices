package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.DocumentReviewObservationTestSamples.*;
import static com.fanflip.admin.domain.IdentityDocumentReviewTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentReviewObservationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentReviewObservation.class);
        DocumentReviewObservation documentReviewObservation1 = getDocumentReviewObservationSample1();
        DocumentReviewObservation documentReviewObservation2 = new DocumentReviewObservation();
        assertThat(documentReviewObservation1).isNotEqualTo(documentReviewObservation2);

        documentReviewObservation2.setId(documentReviewObservation1.getId());
        assertThat(documentReviewObservation1).isEqualTo(documentReviewObservation2);

        documentReviewObservation2 = getDocumentReviewObservationSample2();
        assertThat(documentReviewObservation1).isNotEqualTo(documentReviewObservation2);
    }

    @Test
    void reviewTest() throws Exception {
        DocumentReviewObservation documentReviewObservation = getDocumentReviewObservationRandomSampleGenerator();
        IdentityDocumentReview identityDocumentReviewBack = getIdentityDocumentReviewRandomSampleGenerator();

        documentReviewObservation.setReview(identityDocumentReviewBack);
        assertThat(documentReviewObservation.getReview()).isEqualTo(identityDocumentReviewBack);

        documentReviewObservation.review(null);
        assertThat(documentReviewObservation.getReview()).isNull();
    }
}
