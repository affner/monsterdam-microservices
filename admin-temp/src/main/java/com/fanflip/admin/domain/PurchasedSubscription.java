package com.fanflip.admin.domain;

import com.fanflip.admin.domain.enumeration.PurchasedSubscriptionStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PurchasedSubscription.
 */
@Table("purchased_subscription")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "purchasedsubscription")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchasedSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

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

    @NotNull(message = "must not be null")
    @Column("end_date")
    private LocalDate endDate;

    @NotNull(message = "must not be null")
    @Column("subscription_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private PurchasedSubscriptionStatus subscriptionStatus;

    @NotNull(message = "must not be null")
    @Column("viewer_id")
    private Long viewerId;

    @NotNull(message = "must not be null")
    @Column("creator_id")
    private Long creatorId;

    @Transient
    private PaymentTransaction payment;

    @Transient
    private WalletTransaction walletTransaction;

    @Transient
    private CreatorEarning creatorEarning;

    @Transient
    @JsonIgnoreProperties(value = { "selledSubscriptions", "creator" }, allowSetters = true)
    private SubscriptionBundle subscriptionBundle;

    @Transient
    @JsonIgnoreProperties(value = { "purchasedSubscriptions", "creator" }, allowSetters = true)
    private OfferPromotion appliedPromotion;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "userLite",
            "settings",
            "makedReports",
            "receivedReports",
            "assistanceTickets",
            "withdraws",
            "subscriptionBundles",
            "earnings",
            "payments",
            "planOffers",
            "purchasedContents",
            "purchasedSubscriptions",
            "wallets",
            "socialNetworks",
            "commentNotifications",
            "messageNotifications",
            "userMentionNotifications",
            "commentMentionNotifications",
            "ownAccountsAssociations",
            "createdEvents",
            "bookMarks",
            "feedbacks",
            "sentMessages",
            "chats",
            "mentions",
            "comments",
            "feeds",
            "votedPolls",
            "videoStories",
            "documents",
            "countryOfBirth",
            "stateOfResidence",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "blockedUbications",
            "hashTags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
            "contentPackageTags",
        },
        allowSetters = true
    )
    private UserProfile viewer;

    @Column("payment_id")
    private Long paymentId;

    @Column("wallet_transaction_id")
    private Long walletTransactionId;

    @Column("creator_earning_id")
    private Long creatorEarningId;

    @Column("subscription_bundle_id")
    private Long subscriptionBundleId;

    @Column("applied_promotion_id")
    private Long appliedPromotionId;

    @Column("viewer_id")
    private Long viewerId;

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
        this.paymentId = paymentTransaction != null ? paymentTransaction.getId() : null;
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
        this.walletTransactionId = walletTransaction != null ? walletTransaction.getId() : null;
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
        this.creatorEarningId = creatorEarning != null ? creatorEarning.getId() : null;
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
        this.subscriptionBundleId = subscriptionBundle != null ? subscriptionBundle.getId() : null;
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
        this.appliedPromotionId = offerPromotion != null ? offerPromotion.getId() : null;
    }

    public PurchasedSubscription appliedPromotion(OfferPromotion offerPromotion) {
        this.setAppliedPromotion(offerPromotion);
        return this;
    }

    public UserProfile getViewer() {
        return this.viewer;
    }

    public void setViewer(UserProfile userProfile) {
        this.viewer = userProfile;
        this.viewerId = userProfile != null ? userProfile.getId() : null;
    }

    public PurchasedSubscription viewer(UserProfile userProfile) {
        this.setViewer(userProfile);
        return this;
    }

    public Long getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(Long paymentTransaction) {
        this.paymentId = paymentTransaction;
    }

    public Long getWalletTransactionId() {
        return this.walletTransactionId;
    }

    public void setWalletTransactionId(Long walletTransaction) {
        this.walletTransactionId = walletTransaction;
    }

    public Long getCreatorEarningId() {
        return this.creatorEarningId;
    }

    public void setCreatorEarningId(Long creatorEarning) {
        this.creatorEarningId = creatorEarning;
    }

    public Long getSubscriptionBundleId() {
        return this.subscriptionBundleId;
    }

    public void setSubscriptionBundleId(Long subscriptionBundle) {
        this.subscriptionBundleId = subscriptionBundle;
    }

    public Long getAppliedPromotionId() {
        return this.appliedPromotionId;
    }

    public void setAppliedPromotionId(Long offerPromotion) {
        this.appliedPromotionId = offerPromotion;
    }

    public Long getViewerId() {
        return this.viewerId;
    }

    public void setViewerId(Long userProfile) {
        this.viewerId = userProfile;
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
