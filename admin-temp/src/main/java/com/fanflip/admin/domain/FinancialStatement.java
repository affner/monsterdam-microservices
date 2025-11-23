package com.fanflip.admin.domain;

import com.fanflip.admin.domain.enumeration.StatementType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A FinancialStatement.
 */
@Table("financial_statement")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "financialstatement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinancialStatement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("statement_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private StatementType statementType;

    @NotNull(message = "must not be null")
    @Column("period_start_date")
    private LocalDate periodStartDate;

    @NotNull(message = "must not be null")
    @Column("period_end_date")
    private LocalDate periodEndDate;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private Instant createdDate;

    @Transient
    @JsonIgnoreProperties(
        value = { "payment", "budget", "asset", "liability", "taxDeclarations", "financialStatements" },
        allowSetters = true
    )
    private Set<AccountingRecord> accountingRecords = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FinancialStatement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatementType getStatementType() {
        return this.statementType;
    }

    public FinancialStatement statementType(StatementType statementType) {
        this.setStatementType(statementType);
        return this;
    }

    public void setStatementType(StatementType statementType) {
        this.statementType = statementType;
    }

    public LocalDate getPeriodStartDate() {
        return this.periodStartDate;
    }

    public FinancialStatement periodStartDate(LocalDate periodStartDate) {
        this.setPeriodStartDate(periodStartDate);
        return this;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public LocalDate getPeriodEndDate() {
        return this.periodEndDate;
    }

    public FinancialStatement periodEndDate(LocalDate periodEndDate) {
        this.setPeriodEndDate(periodEndDate);
        return this;
    }

    public void setPeriodEndDate(LocalDate periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public FinancialStatement createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Set<AccountingRecord> getAccountingRecords() {
        return this.accountingRecords;
    }

    public void setAccountingRecords(Set<AccountingRecord> accountingRecords) {
        this.accountingRecords = accountingRecords;
    }

    public FinancialStatement accountingRecords(Set<AccountingRecord> accountingRecords) {
        this.setAccountingRecords(accountingRecords);
        return this;
    }

    public FinancialStatement addAccountingRecords(AccountingRecord accountingRecord) {
        this.accountingRecords.add(accountingRecord);
        return this;
    }

    public FinancialStatement removeAccountingRecords(AccountingRecord accountingRecord) {
        this.accountingRecords.remove(accountingRecord);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinancialStatement)) {
            return false;
        }
        return getId() != null && getId().equals(((FinancialStatement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinancialStatement{" +
            "id=" + getId() +
            ", statementType='" + getStatementType() + "'" +
            ", periodStartDate='" + getPeriodStartDate() + "'" +
            ", periodEndDate='" + getPeriodEndDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
