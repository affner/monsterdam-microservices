package com.monsterdam.finance.domain;

import com.monsterdam.finance.domain.enumeration.PurchasedSubscriptionStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PurchasedSubscription.
 */
@Entity
@Table(name = "purchased_subscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchasedSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status", nullable = false)
    private PurchasedSubscriptionStatus subscriptionStatus;

    @NotNull
    @Column(name = "viewer_id", nullable = false)
    private Long viewerId;

    @NotNull
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @JsonIgnoreProperties(value = { "walletTransaction", "purchasedTip", "purchasedContent", "purchasedSubscription" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private PaymentTransaction payment;

    @JsonIgnoreProperties(value = { "payment", "purchasedTip", "purchasedContent", "purchasedSubscription" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private WalletTransaction walletTransaction;

    @JsonIgnoreProperties(value = { "moneyPayout", "purchasedTip", "purchasedContent", "purchasedSubscription" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private CreatorEarning creatorEarning;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "selledSubscriptions" }, allowSetters = true)
    private SubscriptionBundle subscriptionBundle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "purchasedSubscriptions" }, allowSetters = true)
    private OfferPromotion appliedPromotion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchasedSubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PurchasedSubscription createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public PurchasedSubscription lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PurchasedSubscription createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PurchasedSubscription lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public PurchasedSubscription isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public PurchasedSubscription endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public PurchasedSubscriptionStatus getSubscriptionStatus() {
        return this.subscriptionStatus;
    }

    public PurchasedSubscription subscriptionStatus(PurchasedSubscriptionStatus subscriptionStatus) {
        this.setSubscriptionStatus(subscriptionStatus);
        return this;
    }

    public void setSubscriptionStatus(PurchasedSubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Long getViewerId() {
        return this.viewerId;
    }

    public PurchasedSubscription viewerId(Long viewerId) {
        this.setViewerId(viewerId);
        return this;
    }

    public void setViewerId(Long viewerId) {
        this.viewerId = viewerId;
    }

    public Long getCreatorId() {
        return this.creatorId;
    }

    public PurchasedSubscription creatorId(Long creatorId) {
        this.setCreatorId(creatorId);
        return this;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public PaymentTransaction getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentTransaction paymentTransaction) {
        this.payment = paymentTransaction;
    }

    public PurchasedSubscription payment(PaymentTransaction paymentTransaction) {
        this.setPayment(paymentTransaction);
        return this;
    }

    public WalletTransaction getWalletTransaction() {
        return this.walletTransaction;
    }

    public void setWalletTransaction(WalletTransaction walletTransaction) {
        this.walletTransaction = walletTransaction;
    }

    public PurchasedSubscription walletTransaction(WalletTransaction walletTransaction) {
        this.setWalletTransaction(walletTransaction);
        return this;
    }

    public CreatorEarning getCreatorEarning() {
        return this.creatorEarning;
    }

    public void setCreatorEarning(CreatorEarning creatorEarning) {
        this.creatorEarning = creatorEarning;
    }

    public PurchasedSubscription creatorEarning(CreatorEarning creatorEarning) {
        this.setCreatorEarning(creatorEarning);
        return this;
    }

    public SubscriptionBundle getSubscriptionBundle() {
        return this.subscriptionBundle;
    }

    public void setSubscriptionBundle(SubscriptionBundle subscriptionBundle) {
        this.subscriptionBundle = subscriptionBundle;
    }

    public PurchasedSubscription subscriptionBundle(SubscriptionBundle subscriptionBundle) {
        this.setSubscriptionBundle(subscriptionBundle);
        return this;
    }

    public OfferPromotion getAppliedPromotion() {
        return this.appliedPromotion;
    }

    public void setAppliedPromotion(OfferPromotion offerPromotion) {
        this.appliedPromotion = offerPromotion;
    }

    public PurchasedSubscription appliedPromotion(OfferPromotion offerPromotion) {
        this.setAppliedPromotion(offerPromotion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchasedSubscription)) {
            return false;
        }
        return getId() != null && getId().equals(((PurchasedSubscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchasedSubscription{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", subscriptionStatus='" + getSubscriptionStatus() + "'" +
            ", viewerId=" + getViewerId() +
            ", creatorId=" + getCreatorId() +
            "}";
    }
}
