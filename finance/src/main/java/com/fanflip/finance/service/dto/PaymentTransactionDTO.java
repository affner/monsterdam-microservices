package com.fanflip.finance.service.dto;

import com.fanflip.finance.domain.enumeration.GenericStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.finance.domain.PaymentTransaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Instant paymentDate;

    @NotNull
    private GenericStatus paymentStatus;

    @Size(max = 100)
    private String paymentReference;

    @Size(max = 100)
    private String cloudTransactionId;

    @NotNull
    private Long viewerId;

    private Long paymentMethodId;

    private Long paymentProviderId;

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

    public Long getViewerId() {
        return viewerId;
    }

    public void setViewerId(Long viewerId) {
        this.viewerId = viewerId;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Long getPaymentProviderId() {
        return paymentProviderId;
    }

    public void setPaymentProviderId(Long paymentProviderId) {
        this.paymentProviderId = paymentProviderId;
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
            ", viewerId=" + getViewerId() +
            ", paymentMethodId=" + getPaymentMethodId() +
            ", paymentProviderId=" + getPaymentProviderId() +
            "}";
    }
}
