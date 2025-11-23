package com.fanflip.admin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.DocumentReviewObservation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentReviewObservationDTO implements Serializable {

    private Long id;

    private Instant commentDate;

    @NotNull(message = "must not be null")
    private String comment;

    private IdentityDocumentReviewDTO review;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Instant commentDate) {
        this.commentDate = commentDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public IdentityDocumentReviewDTO getReview() {
        return review;
    }

    public void setReview(IdentityDocumentReviewDTO review) {
        this.review = review;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentReviewObservationDTO)) {
            return false;
        }

        DocumentReviewObservationDTO documentReviewObservationDTO = (DocumentReviewObservationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentReviewObservationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentReviewObservationDTO{" +
            "id=" + getId() +
            ", commentDate='" + getCommentDate() + "'" +
            ", comment='" + getComment() + "'" +
            ", review=" + getReview() +
            "}";
    }
}
