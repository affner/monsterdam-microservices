package com.monsterdam.admin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.PurchasedContent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchasedContentDTO implements Serializable {

    private Long id;

    private Float rating;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    private PaymentTransactionDTO payment;

    private WalletTransactionDTO walletTransaction;

    private CreatorEarningDTO creatorEarning;

    private UserProfileDTO viewer;

    private ContentPackageDTO purchasedContentPackage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
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

    public PaymentTransactionDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentTransactionDTO payment) {
        this.payment = payment;
    }

    public WalletTransactionDTO getWalletTransaction() {
        return walletTransaction;
    }

    public void setWalletTransaction(WalletTransactionDTO walletTransaction) {
        this.walletTransaction = walletTransaction;
    }

    public CreatorEarningDTO getCreatorEarning() {
        return creatorEarning;
    }

    public void setCreatorEarning(CreatorEarningDTO creatorEarning) {
        this.creatorEarning = creatorEarning;
    }

    public UserProfileDTO getViewer() {
        return viewer;
    }

    public void setViewer(UserProfileDTO viewer) {
        this.viewer = viewer;
    }

    public ContentPackageDTO getPurchasedContentPackage() {
        return purchasedContentPackage;
    }

    public void setPurchasedContentPackage(ContentPackageDTO purchasedContentPackage) {
        this.purchasedContentPackage = purchasedContentPackage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchasedContentDTO)) {
            return false;
        }

        PurchasedContentDTO purchasedContentDTO = (PurchasedContentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, purchasedContentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchasedContentDTO{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", payment=" + getPayment() +
            ", walletTransaction=" + getWalletTransaction() +
            ", creatorEarning=" + getCreatorEarning() +
            ", viewer=" + getViewer() +
            ", purchasedContentPackage=" + getPurchasedContentPackage() +
            "}";
    }
}
