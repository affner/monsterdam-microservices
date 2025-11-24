package com.monsterdam.admin.service.dto;

import com.monsterdam.admin.domain.enumeration.GenericStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.PaymentTransaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentTransactionDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private BigDecimal amount;

    @NotNull(message = "must not be null")
    private Instant paymentDate;

    @NotNull(message = "must not be null")
    private GenericStatus paymentStatus;

    @Size(max = 100)
    private String paymentReference;

    @Size(max = 100)
    private String cloudTransactionId;

    private PaymentMethodDTO paymentMethod;

    private PaymentProviderDTO paymentProvider;

    private UserProfileDTO viewer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public GenericStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(GenericStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getCloudTransactionId() {
        return cloudTransactionId;
    }

    public void setCloudTransactionId(String cloudTransactionId) {
        this.cloudTransactionId = cloudTransactionId;
    }

    public PaymentMethodDTO getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodDTO paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentProviderDTO getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(PaymentProviderDTO paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public UserProfileDTO getViewer() {
        return viewer;
    }

    public void setViewer(UserProfileDTO viewer) {
        this.viewer = viewer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentTransactionDTO)) {
            return false;
        }

        PaymentTransactionDTO paymentTransactionDTO = (PaymentTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentTransactionDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", paymentReference='" + getPaymentReference() + "'" +
            ", cloudTransactionId='" + getCloudTransactionId() + "'" +
            ", paymentMethod=" + getPaymentMethod() +
            ", paymentProvider=" + getPaymentProvider() +
            ", viewer=" + getViewer() +
            "}";
    }
}
