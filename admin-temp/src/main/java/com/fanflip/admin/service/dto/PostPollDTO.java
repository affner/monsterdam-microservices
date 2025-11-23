package com.fanflip.admin.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.PostPoll} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostPollDTO implements Serializable {

    private Long id;

    @Lob
    private String question;

    @NotNull(message = "must not be null")
    private Boolean isMultiChoice;

    private Instant lastModifiedDate;

    @NotNull(message = "must not be null")
    private LocalDate endDate;

    @NotNull(message = "must not be null")
    private Duration postPollDuration;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getIsMultiChoice() {
        return isMultiChoice;
    }

    public void setIsMultiChoice(Boolean isMultiChoice) {
        this.isMultiChoice = isMultiChoice;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Duration getPostPollDuration() {
        return postPollDuration;
    }

    public void setPostPollDuration(Duration postPollDuration) {
        this.postPollDuration = postPollDuration;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostPollDTO)) {
            return false;
        }

        PostPollDTO postPollDTO = (PostPollDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, postPollDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostPollDTO{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", isMultiChoice='" + getIsMultiChoice() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", postPollDuration='" + getPostPollDuration() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
