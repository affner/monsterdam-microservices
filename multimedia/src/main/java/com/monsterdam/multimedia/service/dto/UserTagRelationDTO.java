package com.monsterdam.multimedia.service.dto;

import com.monsterdam.multimedia.domain.enumeration.TagStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.multimedia.domain.UserTagRelation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserTagRelationDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    @NotNull
    private Long taggedUserId;

    @NotNull
    private TagStatus tagStatus;

    private ContentPackageDTO contentPackage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getTaggedUserId() {
        return taggedUserId;
    }

    public void setTaggedUserId(Long taggedUserId) {
        this.taggedUserId = taggedUserId;
    }

    public TagStatus getTagStatus() {
        return tagStatus;
    }

    public void setTagStatus(TagStatus tagStatus) {
        this.tagStatus = tagStatus;
    }

    public ContentPackageDTO getContentPackage() {
        return contentPackage;
    }

    public void setContentPackage(ContentPackageDTO contentPackage) {
        this.contentPackage = contentPackage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserTagRelationDTO)) {
            return false;
        }

        UserTagRelationDTO userTagRelationDTO = (UserTagRelationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userTagRelationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserTagRelationDTO{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", taggedUserId=" + getTaggedUserId() +
            ", tagStatus='" + getTagStatus() + "'" +
            ", contentPackage=" + getContentPackage() +
            "}";
    }
}
