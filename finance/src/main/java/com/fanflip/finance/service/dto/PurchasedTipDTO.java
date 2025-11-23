package com.fanflip.finance.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.finance.domain.PurchasedTip} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchasedTipDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    private Long messageId;

    private PaymentTransactionDTO payment;

    private WalletTransactionDTO walletTransaction;

    private CreatorEarningDTO creatorEarning;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchasedTipDTO)) {
            return false;
        }

        PurchasedTipDTO purchasedTipDTO = (PurchasedTipDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, purchasedTipDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchasedTipDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", messageId=" + getMessageId() +
            ", payment=" + getPayment() +
            ", walletTransaction=" + getWalletTransaction() +
            ", creatorEarning=" + getCreatorEarning() +
            "}";
    }
}
