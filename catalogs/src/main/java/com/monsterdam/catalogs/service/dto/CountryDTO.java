package com.monsterdam.catalogs.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.catalogs.domain.Country} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CountryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Size(min = 2, max = 2)
    private String alpha2Code;

    @NotNull
    @Size(min = 3, max = 3)
    private String alpha3Code;

    private String phoneCode;

    @Lob
    private byte[] thumbnailCountry;

    private String thumbnailCountryContentType;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    public void setAlpha3Code(String alpha3Code) {
        this.alpha3Code = alpha3Code;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public byte[] getThumbnailCountry() {
        return thumbnailCountry;
    }

    public void setThumbnailCountry(byte[] thumbnailCountry) {
        this.thumbnailCountry = thumbnailCountry;
    }

    public String getThumbnailCountryContentType() {
        return thumbnailCountryContentType;
    }

    public void setThumbnailCountryContentType(String thumbnailCountryContentType) {
        this.thumbnailCountryContentType = thumbnailCountryContentType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryDTO)) {
            return false;
        }

        CountryDTO countryDTO = (CountryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, countryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", alpha2Code='" + getAlpha2Code() + "'" +
            ", alpha3Code='" + getAlpha3Code() + "'" +
            ", phoneCode='" + getPhoneCode() + "'" +
            ", thumbnailCountry='" + getThumbnailCountry() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
