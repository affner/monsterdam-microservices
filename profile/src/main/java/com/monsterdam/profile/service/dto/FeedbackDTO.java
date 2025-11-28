package com.monsterdam.profile.service.dto;

import com.monsterdam.profile.domain.enumeration.FeedbackType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.profile.domain.Feedback} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackDTO implements Serializable {

    private Long id;

    @NotNull
    private String content;

    @NotNull
    private Instant feedbackDate;

    private Integer feedbackRating;

    private FeedbackType feedbackType;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    @NotNull
    private Long creatorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(Instant feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public Integer getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(Integer feedbackRating) {
        this.feedbackRating = feedbackRating;
    }

    public FeedbackType getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackDTO)) {
            return false;
        }

        FeedbackDTO feedbackDTO = (FeedbackDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, feedbackDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", feedbackDate='" + getFeedbackDate() + "'" +
            ", feedbackRating=" + getFeedbackRating() +
            ", feedbackType='" + getFeedbackType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", creatorId=" + getCreatorId() +
            "}";
    }
}
