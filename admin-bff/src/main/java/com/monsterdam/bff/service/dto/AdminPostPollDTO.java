package com.monsterdam.bff.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO for BFF data handling.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdminPostPollDTO implements Serializable {

    private Long id;

    @Lob
    private String question;

    @NotNull
    private Boolean isMultiChoice;

    private Instant lastModifiedDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Duration postPollDuration;

    @NotNull
    private Instant createdDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
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
        if (!(o instanceof AdminPostPollDTO)) {
            return false;
        }

        AdminPostPollDTO postPollDTO = (AdminPostPollDTO) o;
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
