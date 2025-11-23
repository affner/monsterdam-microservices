package com.fanflip.admin.service.dto;

import com.fanflip.admin.domain.enumeration.UserEventStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.UserEvent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserEventDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String title;

    @Lob
    private String description;

    @NotNull(message = "must not be null")
    private LocalDate startDate;

    @NotNull(message = "must not be null")
    private LocalDate endDate;

    private UserEventStatus creatorEventStatus;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    private UserProfileDTO creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public UserEventStatus getCreatorEventStatus() {
        return creatorEventStatus;
    }

    public void setCreatorEventStatus(UserEventStatus creatorEventStatus) {
        this.creatorEventStatus = creatorEventStatus;
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

    public UserProfileDTO getCreator() {
        return creator;
    }

    public void setCreator(UserProfileDTO creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEventDTO)) {
            return false;
        }

        UserEventDTO userEventDTO = (UserEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserEventDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", creatorEventStatus='" + getCreatorEventStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", creator=" + getCreator() +
            "}";
    }
}
