package com.monsterdam.admin.domain;

import com.monsterdam.admin.domain.enumeration.TaxDeclarationStatus;
import com.monsterdam.admin.domain.enumeration.TaxDeclarationType;
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
 * A TaxDeclaration.
 */
@Table("tax_declaration")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "taxdeclaration")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaxDeclaration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("year")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer year;

    @NotNull(message = "must not be null")
    @Column("declaration_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TaxDeclarationType declarationType;

    @Column("submitted_date")
    private Instant submittedDate;

    @NotNull(message = "must not be null")
    @Column("status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TaxDeclarationStatus status;

    @Column("total_income")
    private BigDecimal totalIncome;

    @Column("total_taxable_income")
    private BigDecimal totalTaxableIncome;

    @Column("total_tax_paid")
    private BigDecimal totalTaxPaid;

    @Column("supporting_documents_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String supportingDocumentsKey;

    @Transient
    @JsonIgnoreProperties(value = { "budget", "asset", "liability", "taxDeclarations", "financialStatements" }, allowSetters = true)
    private Set<AccountingRecord> accountingRecords = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TaxDeclaration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return this.year;
    }

    public TaxDeclaration year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public TaxDeclarationType getDeclarationType() {
        return this.declarationType;
    }

    public TaxDeclaration declarationType(TaxDeclarationType declarationType) {
        this.setDeclarationType(declarationType);
        return this;
    }

    public void setDeclarationType(TaxDeclarationType declarationType) {
        this.declarationType = declarationType;
    }

    public Instant getSubmittedDate() {
        return this.submittedDate;
    }

    public TaxDeclaration submittedDate(Instant submittedDate) {
        this.setSubmittedDate(submittedDate);
        return this;
    }

    public void setSubmittedDate(Instant submittedDate) {
        this.submittedDate = submittedDate;
    }

    public TaxDeclarationStatus getStatus() {
        return this.status;
    }

    public TaxDeclaration status(TaxDeclarationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TaxDeclarationStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalIncome() {
        return this.totalIncome;
    }

    public TaxDeclaration totalIncome(BigDecimal totalIncome) {
        this.setTotalIncome(totalIncome);
        return this;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome != null ? totalIncome.stripTrailingZeros() : null;
    }

    public BigDecimal getTotalTaxableIncome() {
        return this.totalTaxableIncome;
    }

    public TaxDeclaration totalTaxableIncome(BigDecimal totalTaxableIncome) {
        this.setTotalTaxableIncome(totalTaxableIncome);
        return this;
    }

    public void setTotalTaxableIncome(BigDecimal totalTaxableIncome) {
        this.totalTaxableIncome = totalTaxableIncome != null ? totalTaxableIncome.stripTrailingZeros() : null;
    }

    public BigDecimal getTotalTaxPaid() {
        return this.totalTaxPaid;
    }

    public TaxDeclaration totalTaxPaid(BigDecimal totalTaxPaid) {
        this.setTotalTaxPaid(totalTaxPaid);
        return this;
    }

    public void setTotalTaxPaid(BigDecimal totalTaxPaid) {
        this.totalTaxPaid = totalTaxPaid != null ? totalTaxPaid.stripTrailingZeros() : null;
    }

    public String getSupportingDocumentsKey() {
        return this.supportingDocumentsKey;
    }

    public TaxDeclaration supportingDocumentsKey(String supportingDocumentsKey) {
        this.setSupportingDocumentsKey(supportingDocumentsKey);
        return this;
    }

    public void setSupportingDocumentsKey(String supportingDocumentsKey) {
        this.supportingDocumentsKey = supportingDocumentsKey;
    }

    public Set<AccountingRecord> getAccountingRecords() {
        return this.accountingRecords;
    }

    public void setAccountingRecords(Set<AccountingRecord> accountingRecords) {
        this.accountingRecords = accountingRecords;
    }

    public TaxDeclaration accountingRecords(Set<AccountingRecord> accountingRecords) {
        this.setAccountingRecords(accountingRecords);
        return this;
    }

    public TaxDeclaration addAccountingRecords(AccountingRecord accountingRecord) {
        this.accountingRecords.add(accountingRecord);
        return this;
    }

    public TaxDeclaration removeAccountingRecords(AccountingRecord accountingRecord) {
        this.accountingRecords.remove(accountingRecord);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxDeclaration)) {
            return false;
        }
        return getId() != null && getId().equals(((TaxDeclaration) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxDeclaration{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", declarationType='" + getDeclarationType() + "'" +
            ", submittedDate='" + getSubmittedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalIncome=" + getTotalIncome() +
            ", totalTaxableIncome=" + getTotalTaxableIncome() +
            ", totalTaxPaid=" + getTotalTaxPaid() +
            ", supportingDocumentsKey='" + getSupportingDocumentsKey() + "'" +
            "}";
    }
}
