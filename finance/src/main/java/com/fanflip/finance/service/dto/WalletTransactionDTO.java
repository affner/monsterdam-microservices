package com.monsterdam.finance.service.dto;

import com.monsterdam.finance.domain.enumeration.WalletTransactionType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.finance.domain.WalletTransaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WalletTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal amount;

    private Instant lastModifiedDate;

    @NotNull
    private WalletTransactionType transactionType;

    @NotNull
    private Instant createdDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    @NotNull
    private Long viewerId;

    private PaymentTransactionDTO payment;

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

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public WalletTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(WalletTransactionType transactionType) {
        this.transactionType = transactionType;
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

    public Long getViewerId() {
        return viewerId;
    }

    public void setViewerId(Long viewerId) {
        this.viewerId = viewerId;
    }

    public PaymentTransactionDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentTransactionDTO payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WalletTransactionDTO)) {
            return false;
        }

        WalletTransactionDTO walletTransactionDTO = (WalletTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, walletTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WalletTransactionDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", viewerId=" + getViewerId() +
            ", payment=" + getPayment() +
            "}";
    }
}
