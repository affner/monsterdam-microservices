package com.monsterdam.admin.service.dto;

import com.monsterdam.admin.domain.enumeration.StatementType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.FinancialStatement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinancialStatementDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private StatementType statementType;

    @NotNull(message = "must not be null")
    private LocalDate periodStartDate;

    @NotNull(message = "must not be null")
    private LocalDate periodEndDate;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Set<AccountingRecordDTO> accountingRecords = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatementType getStatementType() {
        return statementType;
    }

    public void setStatementType(StatementType statementType) {
        this.statementType = statementType;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public LocalDate getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(LocalDate periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Set<AccountingRecordDTO> getAccountingRecords() {
        return accountingRecords;
    }

    public void setAccountingRecords(Set<AccountingRecordDTO> accountingRecords) {
        this.accountingRecords = accountingRecords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinancialStatementDTO)) {
            return false;
        }

        FinancialStatementDTO financialStatementDTO = (FinancialStatementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, financialStatementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinancialStatementDTO{" +
            "id=" + getId() +
            ", statementType='" + getStatementType() + "'" +
            ", periodStartDate='" + getPeriodStartDate() + "'" +
            ", periodEndDate='" + getPeriodEndDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", accountingRecords=" + getAccountingRecords() +
            "}";
    }
}
