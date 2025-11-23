package com.fanflip.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DocumentReviewObservation.
 */
@Table("document_review_observation")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "documentreviewobservation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentReviewObservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("comment_date")
    private Instant commentDate;

    @NotNull(message = "must not be null")
    @Column("comment")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String comment;

    @Transient
    @JsonIgnoreProperties(value = { "ticket", "documents", "observations" }, allowSetters = true)
    private IdentityDocumentReview review;

    @Column("review_id")
    private Long reviewId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentReviewObservation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCommentDate() {
        return this.commentDate;
    }

    public DocumentReviewObservation commentDate(Instant commentDate) {
        this.setCommentDate(commentDate);
        return this;
    }

    public void setCommentDate(Instant commentDate) {
        this.commentDate = commentDate;
    }

    public String getComment() {
        return this.comment;
    }

    public DocumentReviewObservation comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public IdentityDocumentReview getReview() {
        return this.review;
    }

    public void setReview(IdentityDocumentReview identityDocumentReview) {
        this.review = identityDocumentReview;
        this.reviewId = identityDocumentReview != null ? identityDocumentReview.getId() : null;
    }

    public DocumentReviewObservation review(IdentityDocumentReview identityDocumentReview) {
        this.setReview(identityDocumentReview);
        return this;
    }

    public Long getReviewId() {
        return this.reviewId;
    }

    public void setReviewId(Long identityDocumentReview) {
        this.reviewId = identityDocumentReview;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentReviewObservation)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentReviewObservation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentReviewObservation{" +
            "id=" + getId() +
            ", commentDate='" + getCommentDate() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
