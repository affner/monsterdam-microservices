package com.fanflip.admin.domain;

import com.fanflip.admin.domain.enumeration.AccountingType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AccountingRecord.
 */
@Table("accounting_record")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accountingrecord")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountingRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("date")
    private Instant date;

    @NotNull(message = "must not be null")
    @Column("description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column("debit")
    private BigDecimal debit;

    @Column("credit")
    private BigDecimal credit;

    @NotNull(message = "must not be null")
    @Column("balance")
    private BigDecimal balance;

    @NotNull(message = "must not be null")
    @Column("account_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AccountingType accountType;

    @Transient
    private PaymentTransaction payment;

    @Transient
    @JsonIgnoreProperties(value = { "accountingRecords" }, allowSetters = true)
    private Budget budget;

    @Transient
    @JsonIgnoreProperties(value = { "accountingRecords" }, allowSetters = true)
    private Asset asset;

    @Transient
    @JsonIgnoreProperties(value = { "accountingRecords" }, allowSetters = true)
    private Liability liability;

    @Transient
    @JsonIgnoreProperties(value = { "accountingRecords" }, allowSetters = true)
    private Set<TaxDeclaration> taxDeclarations = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "accountingRecords" }, allowSetters = true)
    private Set<FinancialStatement> financialStatements = new HashSet<>();

    @Column("payment_id")
    private Long paymentId;

    @Column("budget_id")
    private Long budgetId;

    @Column("asset_id")
    private Long assetId;

    @Column("liability_id")
    private Long liabilityId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountingRecord id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public AccountingRecord date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public AccountingRecord description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDebit() {
        return this.debit;
    }

    public AccountingRecord debit(BigDecimal debit) {
        this.setDebit(debit);
        return this;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit != null ? debit.stripTrailingZeros() : null;
    }

    public BigDecimal getCredit() {
        return this.credit;
    }

    public AccountingRecord credit(BigDecimal credit) {
        this.setCredit(credit);
        return this;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit != null ? credit.stripTrailingZeros() : null;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public AccountingRecord balance(BigDecimal balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance != null ? balance.stripTrailingZeros() : null;
    }

    public AccountingType getAccountType() {
        return this.accountType;
    }

    public AccountingRecord accountType(AccountingType accountType) {
        this.setAccountType(accountType);
        return this;
    }

    public void setAccountType(AccountingType accountType) {
        this.accountType = accountType;
    }

    public PaymentTransaction getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentTransaction paymentTransaction) {
        this.payment = paymentTransaction;
        this.paymentId = paymentTransaction != null ? paymentTransaction.getId() : null;
    }

    public AccountingRecord payment(PaymentTransaction paymentTransaction) {
        this.setPayment(paymentTransaction);
        return this;
    }

    public Budget getBudget() {
        return this.budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
        this.budgetId = budget != null ? budget.getId() : null;
    }

    public AccountingRecord budget(Budget budget) {
        this.setBudget(budget);
        return this;
    }

    public Asset getAsset() {
        return this.asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
        this.assetId = asset != null ? asset.getId() : null;
    }

    public AccountingRecord asset(Asset asset) {
        this.setAsset(asset);
        return this;
    }

    public Liability getLiability() {
        return this.liability;
    }

    public void setLiability(Liability liability) {
        this.liability = liability;
        this.liabilityId = liability != null ? liability.getId() : null;
    }

    public AccountingRecord liability(Liability liability) {
        this.setLiability(liability);
        return this;
    }

    public Set<TaxDeclaration> getTaxDeclarations() {
        return this.taxDeclarations;
    }

    public void setTaxDeclarations(Set<TaxDeclaration> taxDeclarations) {
        if (this.taxDeclarations != null) {
            this.taxDeclarations.forEach(i -> i.removeAccountingRecords(this));
        }
        if (taxDeclarations != null) {
            taxDeclarations.forEach(i -> i.addAccountingRecords(this));
        }
        this.taxDeclarations = taxDeclarations;
    }

    public AccountingRecord taxDeclarations(Set<TaxDeclaration> taxDeclarations) {
        this.setTaxDeclarations(taxDeclarations);
        return this;
    }

    public AccountingRecord addTaxDeclarations(TaxDeclaration taxDeclaration) {
        this.taxDeclarations.add(taxDeclaration);
        taxDeclaration.getAccountingRecords().add(this);
        return this;
    }

    public AccountingRecord removeTaxDeclarations(TaxDeclaration taxDeclaration) {
        this.taxDeclarations.remove(taxDeclaration);
        taxDeclaration.getAccountingRecords().remove(this);
        return this;
    }

    public Set<FinancialStatement> getFinancialStatements() {
        return this.financialStatements;
    }

    public void setFinancialStatements(Set<FinancialStatement> financialStatements) {
        if (this.financialStatements != null) {
            this.financialStatements.forEach(i -> i.removeAccountingRecords(this));
        }
        if (financialStatements != null) {
            financialStatements.forEach(i -> i.addAccountingRecords(this));
        }
        this.financialStatements = financialStatements;
    }

    public AccountingRecord financialStatements(Set<FinancialStatement> financialStatements) {
        this.setFinancialStatements(financialStatements);
        return this;
    }

    public AccountingRecord addFinancialStatements(FinancialStatement financialStatement) {
        this.financialStatements.add(financialStatement);
        financialStatement.getAccountingRecords().add(this);
        return this;
    }

    public AccountingRecord removeFinancialStatements(FinancialStatement financialStatement) {
        this.financialStatements.remove(financialStatement);
        financialStatement.getAccountingRecords().remove(this);
        return this;
    }

    public Long getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(Long paymentTransaction) {
        this.paymentId = paymentTransaction;
    }

    public Long getBudgetId() {
        return this.budgetId;
    }

    public void setBudgetId(Long budget) {
        this.budgetId = budget;
    }

    public Long getAssetId() {
        return this.assetId;
    }

    public void setAssetId(Long asset) {
        this.assetId = asset;
    }

    public Long getLiabilityId() {
        return this.liabilityId;
    }

    public void setLiabilityId(Long liability) {
        this.liabilityId = liability;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountingRecord)) {
            return false;
        }
        return getId() != null && getId().equals(((AccountingRecord) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountingRecord{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", debit=" + getDebit() +
            ", credit=" + getCredit() +
            ", balance=" + getBalance() +
            ", accountType='" + getAccountType() + "'" +
            "}";
    }
}
