package com.fanflip.admin.service.dto;

import com.fanflip.admin.domain.enumeration.OfferPromotionType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.OfferPromotion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OfferPromotionDTO implements Serializable {

    private Long id;

    private Duration freeDaysDuration;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private Float discountPercentage;

    @NotNull(message = "must not be null")
    private LocalDate startDate;

    @NotNull(message = "must not be null")
    private LocalDate endDate;

    private Integer subscriptionsLimit;

    @NotNull(message = "must not be null")
    private String linkCode;

    @NotNull(message = "must not be null")
    private Boolean isFinished;

    @NotNull(message = "must not be null")
    private OfferPromotionType promotionType;

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

    public Duration getFreeDaysDuration() {
        return freeDaysDuration;
    }

    public void setFreeDaysDuration(Duration freeDaysDuration) {
        this.freeDaysDuration = freeDaysDuration;
    }

    public Float getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Float discountPercentage) {
        this.discountPercentage = discountPercentage;
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

    public Integer getSubscriptionsLimit() {
        return subscriptionsLimit;
    }

    public void setSubscriptionsLimit(Integer subscriptionsLimit) {
        this.subscriptionsLimit = subscriptionsLimit;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }

    public Boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public OfferPromotionType getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(OfferPromotionType promotionType) {
        this.promotionType = promotionType;
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
        if (!(o instanceof OfferPromotionDTO)) {
            return false;
        }

        OfferPromotionDTO offerPromotionDTO = (OfferPromotionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, offerPromotionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OfferPromotionDTO{" +
            "id=" + getId() +
            ", freeDaysDuration='" + getFreeDaysDuration() + "'" +
            ", discountPercentage=" + getDiscountPercentage() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", subscriptionsLimit=" + getSubscriptionsLimit() +
            ", linkCode='" + getLinkCode() + "'" +
            ", isFinished='" + getIsFinished() + "'" +
            ", promotionType='" + getPromotionType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", creator=" + getCreator() +
            "}";
    }
}
