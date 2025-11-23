package com.fanflip.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PaymentProvider.
 */
@Table("payment_provider")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "paymentprovider")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("provider_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String providerName;

    @Column("description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @NotNull(message = "must not be null")
    @Column("api_key_text")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String apiKeyText;

    @NotNull(message = "must not be null")
    @Column("api_secret_text")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String apiSecretText;

    @NotNull(message = "must not be null")
    @Column("endpoint_text")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String endpointText;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private Instant createdDate;

    @Column("last_modified_date")
    private Instant lastModifiedDate;

    @Column("created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    @Column("last_modified_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isDeleted;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "paymentMethod",
            "paymentProvider",
            "viewer",
            "accountingRecord",
            "purchasedContent",
            "purchasedSubscription",
            "walletTransaction",
            "purchasedTip",
        },
        allowSetters = true
    )
    private Set<PaymentTransaction> payments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentProvider id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderName() {
        return this.providerName;
    }

    public PaymentProvider providerName(String providerName) {
        this.setProviderName(providerName);
        return this;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getDescription() {
        return this.description;
    }

    public PaymentProvider description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApiKeyText() {
        return this.apiKeyText;
    }

    public PaymentProvider apiKeyText(String apiKeyText) {
        this.setApiKeyText(apiKeyText);
        return this;
    }

    public void setApiKeyText(String apiKeyText) {
        this.apiKeyText = apiKeyText;
    }

    public String getApiSecretText() {
        return this.apiSecretText;
    }

    public PaymentProvider apiSecretText(String apiSecretText) {
        this.setApiSecretText(apiSecretText);
        return this;
    }

    public void setApiSecretText(String apiSecretText) {
        this.apiSecretText = apiSecretText;
    }

    public String getEndpointText() {
        return this.endpointText;
    }

    public PaymentProvider endpointText(String endpointText) {
        this.setEndpointText(endpointText);
        return this;
    }

    public void setEndpointText(String endpointText) {
        this.endpointText = endpointText;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PaymentProvider createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public PaymentProvider lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PaymentProvider createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PaymentProvider lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public PaymentProvider isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<PaymentTransaction> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<PaymentTransaction> paymentTransactions) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setPaymentProvider(null));
        }
        if (paymentTransactions != null) {
            paymentTransactions.forEach(i -> i.setPaymentProvider(this));
        }
        this.payments = paymentTransactions;
    }

    public PaymentProvider payments(Set<PaymentTransaction> paymentTransactions) {
        this.setPayments(paymentTransactions);
        return this;
    }

    public PaymentProvider addPayments(PaymentTransaction paymentTransaction) {
        this.payments.add(paymentTransaction);
        paymentTransaction.setPaymentProvider(this);
        return this;
    }

    public PaymentProvider removePayments(PaymentTransaction paymentTransaction) {
        this.payments.remove(paymentTransaction);
        paymentTransaction.setPaymentProvider(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentProvider)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentProvider) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentProvider{" +
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
