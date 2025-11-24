package com.monsterdam.finance.domain;

import com.monsterdam.finance.domain.enumeration.GenericStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentTransaction.
 */
@Entity
@Table(name = "payment_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private GenericStatus paymentStatus;

    @Size(max = 100)
    @Column(name = "payment_reference", length = 100)
    private String paymentReference;

    @Size(max = 100)
    @Column(name = "cloud_transaction_id", length = 100)
    private String cloudTransactionId;

    @NotNull
    @Column(name = "viewer_id", nullable = false)
    private Long viewerId;

    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(name = "payment_provider_id")
    private Long paymentProviderId;

    @JsonIgnoreProperties(value = { "payment", "purchasedTip", "purchasedContent", "purchasedSubscription" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "payment")
    private WalletTransaction walletTransaction;

    @JsonIgnoreProperties(value = { "payment", "walletTransaction", "creatorEarning" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "payment")
    private PurchasedTip purchasedTip;

    @JsonIgnoreProperties(value = { "payment", "walletTransaction", "creatorEarning" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "payment")
    private PurchasedContent purchasedContent;

    @JsonIgnoreProperties(
        value = { "payment", "walletTransaction", "creatorEarning", "subscriptionBundle", "appliedPromotion" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "payment")
    private PurchasedSubscription purchasedSubscription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public PaymentTransaction amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getPaymentDate() {
        return this.paymentDate;
    }

    public PaymentTransaction paymentDate(Instant paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public GenericStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public PaymentTransaction paymentStatus(GenericStatus paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(GenericStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentReference() {
        return this.paymentReference;
    }

    public PaymentTransaction paymentReference(String paymentReference) {
        this.setPaymentReference(paymentReference);
        return this;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getCloudTransactionId() {
        return this.cloudTransactionId;
    }

    public PaymentTransaction cloudTransactionId(String cloudTransactionId) {
        this.setCloudTransactionId(cloudTransactionId);
        return this;
    }

    public void setCloudTransactionId(String cloudTransactionId) {
        this.cloudTransactionId = cloudTransactionId;
    }

    public Long getViewerId() {
        return this.viewerId;
    }

    public PaymentTransaction viewerId(Long viewerId) {
        this.setViewerId(viewerId);
        return this;
    }

    public void setViewerId(Long viewerId) {
        this.viewerId = viewerId;
    }

    public Long getPaymentMethodId() {
        return this.paymentMethodId;
    }

    public PaymentTransaction paymentMethodId(Long paymentMethodId) {
        this.setPaymentMethodId(paymentMethodId);
        return this;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Long getPaymentProviderId() {
        return this.paymentProviderId;
    }

    public PaymentTransaction paymentProviderId(Long paymentProviderId) {
        this.setPaymentProviderId(paymentProviderId);
        return this;
    }

    public void setPaymentProviderId(Long paymentProviderId) {
        this.paymentProviderId = paymentProviderId;
    }

    public WalletTransaction getWalletTransaction() {
        return this.walletTransaction;
    }

    public void setWalletTransaction(WalletTransaction walletTransaction) {
        if (this.walletTransaction != null) {
            this.walletTransaction.setPayment(null);
        }
        if (walletTransaction != null) {
            walletTransaction.setPayment(this);
        }
        this.walletTransaction = walletTransaction;
    }

    public PaymentTransaction walletTransaction(WalletTransaction walletTransaction) {
        this.setWalletTransaction(walletTransaction);
        return this;
    }

    public PurchasedTip getPurchasedTip() {
        return this.purchasedTip;
    }

    public void setPurchasedTip(PurchasedTip purchasedTip) {
        if (this.purchasedTip != null) {
            this.purchasedTip.setPayment(null);
        }
        if (purchasedTip != null) {
            purchasedTip.setPayment(this);
        }
        this.purchasedTip = purchasedTip;
    }

    public PaymentTransaction purchasedTip(PurchasedTip purchasedTip) {
        this.setPurchasedTip(purchasedTip);
        return this;
    }

    public PurchasedContent getPurchasedContent() {
        return this.purchasedContent;
    }

    public void setPurchasedContent(PurchasedContent purchasedContent) {
        if (this.purchasedContent != null) {
            this.purchasedContent.setPayment(null);
        }
        if (purchasedContent != null) {
            purchasedContent.setPayment(this);
        }
        this.purchasedContent = purchasedContent;
    }

    public PaymentTransaction purchasedContent(PurchasedContent purchasedContent) {
        this.setPurchasedContent(purchasedContent);
        return this;
    }

    public PurchasedSubscription getPurchasedSubscription() {
        return this.purchasedSubscription;
    }

    public void setPurchasedSubscription(PurchasedSubscription purchasedSubscription) {
        if (this.purchasedSubscription != null) {
            this.purchasedSubscription.setPayment(null);
        }
        if (purchasedSubscription != null) {
            purchasedSubscription.setPayment(this);
        }
        this.purchasedSubscription = purchasedSubscription;
    }

    public PaymentTransaction purchasedSubscription(PurchasedSubscription purchasedSubscription) {
        this.setPurchasedSubscription(purchasedSubscription);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentTransaction)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentTransaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentTransaction{" +
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
