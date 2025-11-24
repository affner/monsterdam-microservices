package com.monsterdam.admin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.SpecialReward} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpecialRewardDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String description;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    @NotNull(message = "must not be null")
    private Long contentPackageId;

    @NotNull(message = "must not be null")
    private Long viewerId;

    @NotNull(message = "must not be null")
    private Long offerPromotionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getContentPackageId() {
        return contentPackageId;
    }

    public void setContentPackageId(Long contentPackageId) {
        this.contentPackageId = contentPackageId;
    }

    public Long getViewerId() {
        return viewerId;
    }

    public void setViewerId(Long viewerId) {
        this.viewerId = viewerId;
    }

    public Long getOfferPromotionId() {
        return offerPromotionId;
    }

    public void setOfferPromotionId(Long offerPromotionId) {
        this.offerPromotionId = offerPromotionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecialRewardDTO)) {
            return false;
        }

        SpecialRewardDTO specialRewardDTO = (SpecialRewardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, specialRewardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecialRewardDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", contentPackageId=" + getContentPackageId() +
            ", viewerId=" + getViewerId() +
            ", offerPromotionId=" + getOfferPromotionId() +
            "}";
    }
}
