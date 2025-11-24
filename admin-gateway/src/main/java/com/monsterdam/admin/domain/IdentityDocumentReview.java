package com.monsterdam.admin.domain;

import com.monsterdam.admin.domain.enumeration.DocumentStatus;
import com.monsterdam.admin.domain.enumeration.ReviewStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A IdentityDocumentReview.
 */
@Table("identity_document_review")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "identitydocumentreview")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdentityDocumentReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("document_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private DocumentStatus documentStatus;

    @Column("resolution_date")
    private Instant resolutionDate;

    @Column("review_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ReviewStatus reviewStatus;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private Instant createdDate;

    @Column("last_modified_date")
    private Instant lastModifiedDate;

    @Column("created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    @Column("last_modified_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lastModifiedBy;

    @Transient
    private AssistanceTicket ticket;

    @Transient
    @JsonIgnoreProperties(value = { "review" }, allowSetters = true)
    private Set<IdentityDocument> documents = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "review" }, allowSetters = true)
    private Set<DocumentReviewObservation> observations = new HashSet<>();

    @Column("ticket_id")
    private Long ticketId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IdentityDocumentReview id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentStatus getDocumentStatus() {
        return this.documentStatus;
    }

    public IdentityDocumentReview documentStatus(DocumentStatus documentStatus) {
        this.setDocumentStatus(documentStatus);
        return this;
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public Instant getResolutionDate() {
        return this.resolutionDate;
    }

    public IdentityDocumentReview resolutionDate(Instant resolutionDate) {
        this.setResolutionDate(resolutionDate);
        return this;
    }

    public void setResolutionDate(Instant resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public ReviewStatus getReviewStatus() {
        return this.reviewStatus;
    }

    public IdentityDocumentReview reviewStatus(ReviewStatus reviewStatus) {
        this.setReviewStatus(reviewStatus);
        return this;
    }

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public IdentityDocumentReview createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public IdentityDocumentReview lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public IdentityDocumentReview createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public IdentityDocumentReview lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public AssistanceTicket getTicket() {
        return this.ticket;
    }

    public void setTicket(AssistanceTicket assistanceTicket) {
        this.ticket = assistanceTicket;
        this.ticketId = assistanceTicket != null ? assistanceTicket.getId() : null;
    }

    public IdentityDocumentReview ticket(AssistanceTicket assistanceTicket) {
        this.setTicket(assistanceTicket);
        return this;
    }

    public Set<IdentityDocument> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<IdentityDocument> identityDocuments) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setReview(null));
        }
        if (identityDocuments != null) {
            identityDocuments.forEach(i -> i.setReview(this));
        }
        this.documents = identityDocuments;
    }

    public IdentityDocumentReview documents(Set<IdentityDocument> identityDocuments) {
        this.setDocuments(identityDocuments);
        return this;
    }

    public IdentityDocumentReview addDocuments(IdentityDocument identityDocument) {
        this.documents.add(identityDocument);
        identityDocument.setReview(this);
        return this;
    }

    public IdentityDocumentReview removeDocuments(IdentityDocument identityDocument) {
        this.documents.remove(identityDocument);
        identityDocument.setReview(null);
        return this;
    }

    public Set<DocumentReviewObservation> getObservations() {
        return this.observations;
    }

    public void setObservations(Set<DocumentReviewObservation> documentReviewObservations) {
        if (this.observations != null) {
            this.observations.forEach(i -> i.setReview(null));
        }
        if (documentReviewObservations != null) {
            documentReviewObservations.forEach(i -> i.setReview(this));
        }
        this.observations = documentReviewObservations;
    }

    public IdentityDocumentReview observations(Set<DocumentReviewObservation> documentReviewObservations) {
        this.setObservations(documentReviewObservations);
        return this;
    }

    public IdentityDocumentReview addObservations(DocumentReviewObservation documentReviewObservation) {
        this.observations.add(documentReviewObservation);
        documentReviewObservation.setReview(this);
        return this;
    }

    public IdentityDocumentReview removeObservations(DocumentReviewObservation documentReviewObservation) {
        this.observations.remove(documentReviewObservation);
        documentReviewObservation.setReview(null);
        return this;
    }

    public Long getTicketId() {
        return this.ticketId;
    }

    public void setTicketId(Long assistanceTicket) {
        this.ticketId = assistanceTicket;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdentityDocumentReview)) {
            return false;
        }
        return getId() != null && getId().equals(((IdentityDocumentReview) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdentityDocumentReview{" +
            "id=" + getId() +
            ", documentStatus='" + getDocumentStatus() + "'" +
            ", resolutionDate='" + getResolutionDate() + "'" +
            ", reviewStatus='" + getReviewStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
