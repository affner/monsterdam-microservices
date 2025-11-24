package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.AssistanceTicketTestSamples.*;
import static com.monsterdam.admin.domain.DocumentReviewObservationTestSamples.*;
import static com.monsterdam.admin.domain.IdentityDocumentReviewTestSamples.*;
import static com.monsterdam.admin.domain.IdentityDocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class IdentityDocumentReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdentityDocumentReview.class);
        IdentityDocumentReview identityDocumentReview1 = getIdentityDocumentReviewSample1();
        IdentityDocumentReview identityDocumentReview2 = new IdentityDocumentReview();
        assertThat(identityDocumentReview1).isNotEqualTo(identityDocumentReview2);

        identityDocumentReview2.setId(identityDocumentReview1.getId());
        assertThat(identityDocumentReview1).isEqualTo(identityDocumentReview2);

        identityDocumentReview2 = getIdentityDocumentReviewSample2();
        assertThat(identityDocumentReview1).isNotEqualTo(identityDocumentReview2);
    }

    @Test
    void ticketTest() throws Exception {
        IdentityDocumentReview identityDocumentReview = getIdentityDocumentReviewRandomSampleGenerator();
        AssistanceTicket assistanceTicketBack = getAssistanceTicketRandomSampleGenerator();

        identityDocumentReview.setTicket(assistanceTicketBack);
        assertThat(identityDocumentReview.getTicket()).isEqualTo(assistanceTicketBack);

        identityDocumentReview.ticket(null);
        assertThat(identityDocumentReview.getTicket()).isNull();
    }

    @Test
    void documentsTest() throws Exception {
        IdentityDocumentReview identityDocumentReview = getIdentityDocumentReviewRandomSampleGenerator();
        IdentityDocument identityDocumentBack = getIdentityDocumentRandomSampleGenerator();

        identityDocumentReview.addDocuments(identityDocumentBack);
        assertThat(identityDocumentReview.getDocuments()).containsOnly(identityDocumentBack);
        assertThat(identityDocumentBack.getReview()).isEqualTo(identityDocumentReview);

        identityDocumentReview.removeDocuments(identityDocumentBack);
        assertThat(identityDocumentReview.getDocuments()).doesNotContain(identityDocumentBack);
        assertThat(identityDocumentBack.getReview()).isNull();

        identityDocumentReview.documents(new HashSet<>(Set.of(identityDocumentBack)));
        assertThat(identityDocumentReview.getDocuments()).containsOnly(identityDocumentBack);
        assertThat(identityDocumentBack.getReview()).isEqualTo(identityDocumentReview);

        identityDocumentReview.setDocuments(new HashSet<>());
        assertThat(identityDocumentReview.getDocuments()).doesNotContain(identityDocumentBack);
        assertThat(identityDocumentBack.getReview()).isNull();
    }

    @Test
    void observationsTest() throws Exception {
        IdentityDocumentReview identityDocumentReview = getIdentityDocumentReviewRandomSampleGenerator();
        DocumentReviewObservation documentReviewObservationBack = getDocumentReviewObservationRandomSampleGenerator();

        identityDocumentReview.addObservations(documentReviewObservationBack);
        assertThat(identityDocumentReview.getObservations()).containsOnly(documentReviewObservationBack);
        assertThat(documentReviewObservationBack.getReview()).isEqualTo(identityDocumentReview);

        identityDocumentReview.removeObservations(documentReviewObservationBack);
        assertThat(identityDocumentReview.getObservations()).doesNotContain(documentReviewObservationBack);
        assertThat(documentReviewObservationBack.getReview()).isNull();

        identityDocumentReview.observations(new HashSet<>(Set.of(documentReviewObservationBack)));
        assertThat(identityDocumentReview.getObservations()).containsOnly(documentReviewObservationBack);
        assertThat(documentReviewObservationBack.getReview()).isEqualTo(identityDocumentReview);

        identityDocumentReview.setObservations(new HashSet<>());
        assertThat(identityDocumentReview.getObservations()).doesNotContain(documentReviewObservationBack);
        assertThat(documentReviewObservationBack.getReview()).isNull();
    }
}
