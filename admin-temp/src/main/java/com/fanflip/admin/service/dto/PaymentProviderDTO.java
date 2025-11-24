package com.monsterdam.admin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.PaymentProvider} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentProviderDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String providerName;

    private String description;

    @NotNull(message = "must not be null")
    private String apiKeyText;

    @NotNull(message = "must not be null")
    private String apiSecretText;

    @NotNull(message = "must not be null")
    private String endpointText;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApiKeyText() {
        return apiKeyText;
    }

    public void setApiKeyText(String apiKeyText) {
        this.apiKeyText = apiKeyText;
    }

    public String getApiSecretText() {
        return apiSecretText;
    }

    public void setApiSecretText(String apiSecretText) {
        this.apiSecretText = apiSecretText;
    }

    public String getEndpointText() {
        return endpointText;
    }

    public void setEndpointText(String endpointText) {
        this.endpointText = endpointText;
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
        if (!(o instanceof PaymentProviderDTO)) {
            return false;
        }

        PaymentProviderDTO paymentProviderDTO = (PaymentProviderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentProviderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentProviderDTO{" +
            "id=" + getId() +
            ", providerName='" + getProviderName() + "'" +
            ", description='" + getDescription() + "'" +
            ", apiKeyText='" + getApiKeyText() + "'" +
            ", apiSecretText='" + getApiSecretText() + "'" +
            ", endpointText='" + getEndpointText() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
