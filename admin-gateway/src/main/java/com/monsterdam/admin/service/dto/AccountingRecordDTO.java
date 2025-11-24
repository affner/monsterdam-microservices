package com.monsterdam.admin.service.dto;

import com.monsterdam.admin.domain.enumeration.AccountingType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.AccountingRecord} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountingRecordDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Instant date;

    @NotNull(message = "must not be null")
    private String description;

    private BigDecimal debit;

    private BigDecimal credit;

    @NotNull(message = "must not be null")
    private BigDecimal balance;

    @NotNull(message = "must not be null")
    private AccountingType accountType;

    private Long paymentId;

    private BudgetDTO budget;

    private AssetDTO asset;

    private LiabilityDTO liability;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountingType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountingType accountType) {
        this.accountType = accountType;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public BudgetDTO getBudget() {
        return budget;
    }

    public void setBudget(BudgetDTO budget) {
        this.budget = budget;
    }

    public AssetDTO getAsset() {
        return asset;
    }

    public void setAsset(AssetDTO asset) {
        this.asset = asset;
    }

    public LiabilityDTO getLiability() {
        return liability;
    }

    public void setLiability(LiabilityDTO liability) {
        this.liability = liability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountingRecordDTO)) {
            return false;
        }

        AccountingRecordDTO accountingRecordDTO = (AccountingRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accountingRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountingRecordDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", debit=" + getDebit() +
            ", credit=" + getCredit() +
            ", balance=" + getBalance() +
            ", accountType='" + getAccountType() + "'" +
            ", paymentId=" + getPaymentId() +
            ", budget=" + getBudget() +
            ", asset=" + getAsset() +
            ", liability=" + getLiability() +
            "}";
    }
}
