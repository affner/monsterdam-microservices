package com.fanflip.finance.service.dto;

import com.fanflip.finance.domain.enumeration.PayoutStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.finance.domain.MoneyPayout} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyPayoutDTO implements Serializable {

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

    @NotNull
    private PayoutStatus withdrawStatus;

    @NotNull
    private Long creatorId;

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

    public PayoutStatus getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(PayoutStatus withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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
        if (!(o instanceof MoneyPayoutDTO)) {
            return false;
        }

        MoneyPayoutDTO moneyPayoutDTO = (MoneyPayoutDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moneyPayoutDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyPayoutDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", withdrawStatus='" + getWithdrawStatus() + "'" +
            ", creatorId=" + getCreatorId() +
            ", creatorEarning=" + getCreatorEarning() +
            "}";
    }
}
