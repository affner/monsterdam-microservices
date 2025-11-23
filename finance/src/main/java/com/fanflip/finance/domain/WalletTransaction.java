package com.fanflip.finance.domain;

import com.fanflip.finance.domain.enumeration.WalletTransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WalletTransaction.
 */
@Entity
@Table(name = "wallet_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WalletTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private WalletTransactionType transactionType;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @NotNull
    @Column(name = "viewer_id", nullable = false)
    private Long viewerId;

    @JsonIgnoreProperties(value = { "walletTransaction", "purchasedTip", "purchasedContent", "purchasedSubscription" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private PaymentTransaction payment;

    @JsonIgnoreProperties(value = { "payment", "walletTransaction", "creatorEarning" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "walletTransaction")
    private PurchasedTip purchasedTip;

    @JsonIgnoreProperties(value = { "payment", "walletTransaction", "creatorEarning" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "walletTransaction")
    private PurchasedContent purchasedContent;

    @JsonIgnoreProperties(
        value = { "payment", "walletTransaction", "creatorEarning", "subscriptionBundle", "appliedPromotion" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "walletTransaction")
    private PurchasedSubscription purchasedSubscription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WalletTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public WalletTransaction amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public WalletTransaction lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public WalletTransactionType getTransactionType() {
        return this.transactionType;
    }

    public WalletTransaction transactionType(WalletTransactionType transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(WalletTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public WalletTransaction createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public WalletTransaction createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public WalletTransaction lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public WalletTransaction isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getViewerId() {
        return this.viewerId;
    }

    public WalletTransaction viewerId(Long viewerId) {
        this.setViewerId(viewerId);
        return this;
    }

    public void setViewerId(Long viewerId) {
        this.viewerId = viewerId;
    }

    public PaymentTransaction getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentTransaction paymentTransaction) {
        this.payment = paymentTransaction;
    }

    public WalletTransaction payment(PaymentTransaction paymentTransaction) {
        this.setPayment(paymentTransaction);
        return this;
    }

    public PurchasedTip getPurchasedTip() {
        return this.purchasedTip;
    }

    public void setPurchasedTip(PurchasedTip purchasedTip) {
        if (this.purchasedTip != null) {
            this.purchasedTip.setWalletTransaction(null);
        }
        if (purchasedTip != null) {
            purchasedTip.setWalletTransaction(this);
        }
        this.purchasedTip = purchasedTip;
    }

    public WalletTransaction purchasedTip(PurchasedTip purchasedTip) {
        this.setPurchasedTip(purchasedTip);
        return this;
    }

    public PurchasedContent getPurchasedContent() {
        return this.purchasedContent;
    }

    public void setPurchasedContent(PurchasedContent purchasedContent) {
        if (this.purchasedContent != null) {
            this.purchasedContent.setWalletTransaction(null);
        }
        if (purchasedContent != null) {
            purchasedContent.setWalletTransaction(this);
        }
        this.purchasedContent = purchasedContent;
    }

    public WalletTransaction purchasedContent(PurchasedContent purchasedContent) {
        this.setPurchasedContent(purchasedContent);
        return this;
    }

    public PurchasedSubscription getPurchasedSubscription() {
        return this.purchasedSubscription;
    }

    public void setPurchasedSubscription(PurchasedSubscription purchasedSubscription) {
        if (this.purchasedSubscription != null) {
            this.purchasedSubscription.setWalletTransaction(null);
        }
        if (purchasedSubscription != null) {
            purchasedSubscription.setWalletTransaction(this);
        }
        this.purchasedSubscription = purchasedSubscription;
    }

    public WalletTransaction purchasedSubscription(PurchasedSubscription purchasedSubscription) {
        this.setPurchasedSubscription(purchasedSubscription);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WalletTransaction)) {
            return false;
        }
        return getId() != null && getId().equals(((WalletTransaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WalletTransaction{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", viewerId=" + getViewerId() +
            "}";
    }
}
