package com.monsterdam.finance.service.dto;

import com.monsterdam.finance.domain.enumeration.PurchasedSubscriptionStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.finance.domain.PurchasedSubscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchasedSubscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private PurchasedSubscriptionStatus subscriptionStatus;

    @NotNull
    private Long viewerId;

    @NotNull
    private Long creatorId;

    private PaymentTransactionDTO payment;

    private WalletTransactionDTO walletTransaction;

    private CreatorEarningDTO creatorEarning;

    private SubscriptionBundleDTO subscriptionBundle;

    private OfferPromotionDTO appliedPromotion;

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

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public PurchasedSubscriptionStatus getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(PurchasedSubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Long getViewerId() {
        return viewerId;
    }

    public void setViewerId(Long viewerId) {
        this.viewerId = viewerId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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

    public SubscriptionBundleDTO getSubscriptionBundle() {
        return subscriptionBundle;
    }

    public void setSubscriptionBundle(SubscriptionBundleDTO subscriptionBundle) {
        this.subscriptionBundle = subscriptionBundle;
    }

    public OfferPromotionDTO getAppliedPromotion() {
        return appliedPromotion;
    }

    public void setAppliedPromotion(OfferPromotionDTO appliedPromotion) {
        this.appliedPromotion = appliedPromotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchasedSubscriptionDTO)) {
            return false;
        }

        PurchasedSubscriptionDTO purchasedSubscriptionDTO = (PurchasedSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, purchasedSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchasedSubscriptionDTO{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", subscriptionStatus='" + getSubscriptionStatus() + "'" +
            ", viewerId=" + getViewerId() +
            ", creatorId=" + getCreatorId() +
            ", payment=" + getPayment() +
            ", walletTransaction=" + getWalletTransaction() +
            ", creatorEarning=" + getCreatorEarning() +
            ", subscriptionBundle=" + getSubscriptionBundle() +
            ", appliedPromotion=" + getAppliedPromotion() +
            "}";
    }
}
