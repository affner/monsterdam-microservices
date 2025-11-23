package com.fanflip.admin.domain;

import com.fanflip.admin.domain.enumeration.AssetType;
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
 * A Asset.
 */
@Table("asset")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "asset")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @NotNull(message = "must not be null")
    @Column("value")
    private BigDecimal value;

    @Column("acquisition_date")
    private LocalDate acquisitionDate;

    @NotNull(message = "must not be null")
    @Column("type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AssetType type;

    @Transient
    @JsonIgnoreProperties(value = { "budget", "asset", "liability", "taxDeclarations", "financialStatements" }, allowSetters = true)
    private Set<AccountingRecord> accountingRecords = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Asset id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Asset name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public Asset value(BigDecimal value) {
        this.setValue(value);
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value != null ? value.stripTrailingZeros() : null;
    }

    public LocalDate getAcquisitionDate() {
        return this.acquisitionDate;
    }

    public Asset acquisitionDate(LocalDate acquisitionDate) {
        this.setAcquisitionDate(acquisitionDate);
        return this;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public AssetType getType() {
        return this.type;
    }

    public Asset type(AssetType type) {
        this.setType(type);
        return this;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    public Set<AccountingRecord> getAccountingRecords() {
        return this.accountingRecords;
    }

    public void setAccountingRecords(Set<AccountingRecord> accountingRecords) {
        if (this.accountingRecords != null) {
            this.accountingRecords.forEach(i -> i.setAsset(null));
        }
        if (accountingRecords != null) {
            accountingRecords.forEach(i -> i.setAsset(this));
        }
        this.accountingRecords = accountingRecords;
    }

    public Asset accountingRecords(Set<AccountingRecord> accountingRecords) {
        this.setAccountingRecords(accountingRecords);
        return this;
    }

    public Asset addAccountingRecords(AccountingRecord accountingRecord) {
        this.accountingRecords.add(accountingRecord);
        accountingRecord.setAsset(this);
        return this;
    }

    public Asset removeAccountingRecords(AccountingRecord accountingRecord) {
        this.accountingRecords.remove(accountingRecord);
        accountingRecord.setAsset(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asset)) {
            return false;
        }
        return getId() != null && getId().equals(((Asset) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Asset{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", value=" + getValue() +
            ", acquisitionDate='" + getAcquisitionDate() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
