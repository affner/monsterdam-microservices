package com.fanflip.admin.service.dto;

import com.fanflip.admin.domain.enumeration.TaxDeclarationStatus;
import com.fanflip.admin.domain.enumeration.TaxDeclarationType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.fanflip.admin.domain.TaxDeclaration} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaxDeclarationDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Integer year;

    @NotNull(message = "must not be null")
    private TaxDeclarationType declarationType;

    private Instant submittedDate;

    @NotNull(message = "must not be null")
    private TaxDeclarationStatus status;

    private BigDecimal totalIncome;

    private BigDecimal totalTaxableIncome;

    private BigDecimal totalTaxPaid;

    private String supportingDocumentsKey;

    private Set<AccountingRecordDTO> accountingRecords = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public TaxDeclarationType getDeclarationType() {
        return declarationType;
    }

    public void setDeclarationType(TaxDeclarationType declarationType) {
        this.declarationType = declarationType;
    }

    public Instant getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Instant submittedDate) {
        this.submittedDate = submittedDate;
    }

    public TaxDeclarationStatus getStatus() {
        return status;
    }

    public void setStatus(TaxDeclarationStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalTaxableIncome() {
        return totalTaxableIncome;
    }

    public void setTotalTaxableIncome(BigDecimal totalTaxableIncome) {
        this.totalTaxableIncome = totalTaxableIncome;
    }

    public BigDecimal getTotalTaxPaid() {
        return totalTaxPaid;
    }

    public void setTotalTaxPaid(BigDecimal totalTaxPaid) {
        this.totalTaxPaid = totalTaxPaid;
    }

    public String getSupportingDocumentsKey() {
        return supportingDocumentsKey;
    }

    public void setSupportingDocumentsKey(String supportingDocumentsKey) {
        this.supportingDocumentsKey = supportingDocumentsKey;
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
        if (!(o instanceof TaxDeclarationDTO)) {
            return false;
        }

        TaxDeclarationDTO taxDeclarationDTO = (TaxDeclarationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taxDeclarationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxDeclarationDTO{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", declarationType='" + getDeclarationType() + "'" +
            ", submittedDate='" + getSubmittedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalIncome=" + getTotalIncome() +
            ", totalTaxableIncome=" + getTotalTaxableIncome() +
            ", totalTaxPaid=" + getTotalTaxPaid() +
            ", supportingDocumentsKey='" + getSupportingDocumentsKey() + "'" +
            ", accountingRecords=" + getAccountingRecords() +
            "}";
    }
}
