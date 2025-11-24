package com.monsterdam.admin.domain;

import com.monsterdam.admin.domain.enumeration.LiabilityType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Liability.
 */
@Table("liability")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "liability")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Liability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @NotNull(message = "must not be null")
    @Column("amount")
    private BigDecimal amount;

    @Column("due_date")
    private LocalDate dueDate;

    @NotNull(message = "must not be null")
    @Column("type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private LiabilityType type;

    @Transient
    @JsonIgnoreProperties(value = { "budget", "asset", "liability", "taxDeclarations", "financialStatements" }, allowSetters = true)
    private Set<AccountingRecord> accountingRecords = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Liability id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Liability name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Liability amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount != null ? amount.stripTrailingZeros() : null;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public Liability dueDate(LocalDate dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LiabilityType getType() {
        return this.type;
    }

    public Liability type(LiabilityType type) {
        this.setType(type);
        return this;
    }

    public void setType(LiabilityType type) {
        this.type = type;
    }

    public Set<AccountingRecord> getAccountingRecords() {
        return this.accountingRecords;
    }

    public void setAccountingRecords(Set<AccountingRecord> accountingRecords) {
        if (this.accountingRecords != null) {
            this.accountingRecords.forEach(i -> i.setLiability(null));
        }
        if (accountingRecords != null) {
            accountingRecords.forEach(i -> i.setLiability(this));
        }
        this.accountingRecords = accountingRecords;
    }

    public Liability accountingRecords(Set<AccountingRecord> accountingRecords) {
        this.setAccountingRecords(accountingRecords);
        return this;
    }

    public Liability addAccountingRecords(AccountingRecord accountingRecord) {
        this.accountingRecords.add(accountingRecord);
        accountingRecord.setLiability(this);
        return this;
    }

    public Liability removeAccountingRecords(AccountingRecord accountingRecord) {
        this.accountingRecords.remove(accountingRecord);
        accountingRecord.setLiability(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Liability)) {
            return false;
        }
        return getId() != null && getId().equals(((Liability) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Liability{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", amount=" + getAmount() +
            ", dueDate='" + getDueDate() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
