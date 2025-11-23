package com.fanflip.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Budget.
 */
@Table("budget")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "budget")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("year")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer year;

    @NotNull(message = "must not be null")
    @Column("total_budget")
    private BigDecimal totalBudget;

    @Column("spent_amount")
    private BigDecimal spentAmount;

    @Column("remaining_amount")
    private BigDecimal remainingAmount;

    @Column("budget_details")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String budgetDetails;

    @Transient
    @JsonIgnoreProperties(value = { "budget", "asset", "liability", "taxDeclarations", "financialStatements" }, allowSetters = true)
    private Set<AccountingRecord> accountingRecords = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Budget id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return this.year;
    }

    public Budget year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getTotalBudget() {
        return this.totalBudget;
    }

    public Budget totalBudget(BigDecimal totalBudget) {
        this.setTotalBudget(totalBudget);
        return this;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget != null ? totalBudget.stripTrailingZeros() : null;
    }

    public BigDecimal getSpentAmount() {
        return this.spentAmount;
    }

    public Budget spentAmount(BigDecimal spentAmount) {
        this.setSpentAmount(spentAmount);
        return this;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount != null ? spentAmount.stripTrailingZeros() : null;
    }

    public BigDecimal getRemainingAmount() {
        return this.remainingAmount;
    }

    public Budget remainingAmount(BigDecimal remainingAmount) {
        this.setRemainingAmount(remainingAmount);
        return this;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount != null ? remainingAmount.stripTrailingZeros() : null;
    }

    public String getBudgetDetails() {
        return this.budgetDetails;
    }

    public Budget budgetDetails(String budgetDetails) {
        this.setBudgetDetails(budgetDetails);
        return this;
    }

    public void setBudgetDetails(String budgetDetails) {
        this.budgetDetails = budgetDetails;
    }

    public Set<AccountingRecord> getAccountingRecords() {
        return this.accountingRecords;
    }

    public void setAccountingRecords(Set<AccountingRecord> accountingRecords) {
        if (this.accountingRecords != null) {
            this.accountingRecords.forEach(i -> i.setBudget(null));
        }
        if (accountingRecords != null) {
            accountingRecords.forEach(i -> i.setBudget(this));
        }
        this.accountingRecords = accountingRecords;
    }

    public Budget accountingRecords(Set<AccountingRecord> accountingRecords) {
        this.setAccountingRecords(accountingRecords);
        return this;
    }

    public Budget addAccountingRecords(AccountingRecord accountingRecord) {
        this.accountingRecords.add(accountingRecord);
        accountingRecord.setBudget(this);
        return this;
    }

    public Budget removeAccountingRecords(AccountingRecord accountingRecord) {
        this.accountingRecords.remove(accountingRecord);
        accountingRecord.setBudget(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Budget)) {
            return false;
        }
        return getId() != null && getId().equals(((Budget) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Budget{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", totalBudget=" + getTotalBudget() +
            ", spentAmount=" + getSpentAmount() +
            ", remainingAmount=" + getRemainingAmount() +
            ", budgetDetails='" + getBudgetDetails() + "'" +
            "}";
    }
}
