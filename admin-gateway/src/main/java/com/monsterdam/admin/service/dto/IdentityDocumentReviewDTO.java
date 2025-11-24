package com.monsterdam.admin.service.dto;

import com.monsterdam.admin.domain.enumeration.DocumentStatus;
import com.monsterdam.admin.domain.enumeration.ReviewStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.IdentityDocumentReview} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdentityDocumentReviewDTO implements Serializable {

    private Long id;

    private DocumentStatus documentStatus;

    private Instant resolutionDate;

    private ReviewStatus reviewStatus;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private AssistanceTicketDTO ticket;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public Instant getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Instant resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public AssistanceTicketDTO getTicket() {
        return ticket;
    }

    public void setTicket(AssistanceTicketDTO ticket) {
        this.ticket = ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdentityDocumentReviewDTO)) {
            return false;
        }

        IdentityDocumentReviewDTO identityDocumentReviewDTO = (IdentityDocumentReviewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, identityDocumentReviewDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdentityDocumentReviewDTO{" +
            "id=" + getId() +
            ", documentStatus='" + getDocumentStatus() + "'" +
            ", resolutionDate='" + getResolutionDate() + "'" +
            ", reviewStatus='" + getReviewStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", ticket=" + getTicket() +
            "}";
    }
}
