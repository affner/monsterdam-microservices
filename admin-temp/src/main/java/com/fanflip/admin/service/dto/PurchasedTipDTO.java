package com.monsterdam.admin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.PurchasedTip} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchasedTipDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private BigDecimal amount;

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

    private DirectMessageDTO message;

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

    public DirectMessageDTO getMessage() {
        return message;
    }

    public void setMessage(DirectMessageDTO message) {
        this.message = message;
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
            ", payment=" + getPayment() +
            ", walletTransaction=" + getWalletTransaction() +
            ", creatorEarning=" + getCreatorEarning() +
            ", message=" + getMessage() +
            "}";
    }
}
