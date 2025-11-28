package com.monsterdam.catalogs.service.dto;

import com.monsterdam.catalogs.domain.enumeration.TaxType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.catalogs.domain.TaxInfo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaxInfoDTO implements Serializable {

    private Long id;

    private Float ratePercentage;

    @NotNull
    private TaxType taxType;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    private CountryDTO country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getRatePercentage() {
        return ratePercentage;
    }

    public void setRatePercentage(Float ratePercentage) {
        this.ratePercentage = ratePercentage;
    }

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
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

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxInfoDTO)) {
            return false;
        }

        TaxInfoDTO taxInfoDTO = (TaxInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taxInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxInfoDTO{" +
            "id=" + getId() +
            ", ratePercentage=" + getRatePercentage() +
            ", taxType='" + getTaxType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", country=" + getCountry() +
            "}";
    }
}
