package com.fanflip.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PurchasedContent.
 */
@Table("purchased_content")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "purchasedcontent")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchasedContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("rating")
    private Float rating;

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
    private PaymentTransaction payment;

    @Transient
    private WalletTransaction walletTransaction;

    @Transient
    private CreatorEarning creatorEarning;

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

    @Transient
    @JsonIgnoreProperties(value = { "audio", "selledPackages", "videos", "photos", "usersTaggeds", "message", "post" }, allowSetters = true)
    private ContentPackage purchasedContentPackage;

    @Column("payment_id")
    private Long paymentId;

    @Column("wallet_transaction_id")
    private Long walletTransactionId;

    @Column("creator_earning_id")
    private Long creatorEarningId;

    @Column("viewer_id")
    private Long viewerId;

    @Column("purchased_content_package_id")
    private Long purchasedContentPackageId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchasedContent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getRating() {
        return this.rating;
    }

    public PurchasedContent rating(Float rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PurchasedContent createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public PurchasedContent lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PurchasedContent createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PurchasedContent lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public PurchasedContent isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public PaymentTransaction getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentTransaction paymentTransaction) {
        this.payment = paymentTransaction;
        this.paymentId = paymentTransaction != null ? paymentTransaction.getId() : null;
    }

    public PurchasedContent payment(PaymentTransaction paymentTransaction) {
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

    public PurchasedContent walletTransaction(WalletTransaction walletTransaction) {
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

    public PurchasedContent creatorEarning(CreatorEarning creatorEarning) {
        this.setCreatorEarning(creatorEarning);
        return this;
    }

    public UserProfile getViewer() {
        return this.viewer;
    }

    public void setViewer(UserProfile userProfile) {
        this.viewer = userProfile;
        this.viewerId = userProfile != null ? userProfile.getId() : null;
    }

    public PurchasedContent viewer(UserProfile userProfile) {
        this.setViewer(userProfile);
        return this;
    }

    public ContentPackage getPurchasedContentPackage() {
        return this.purchasedContentPackage;
    }

    public void setPurchasedContentPackage(ContentPackage contentPackage) {
        this.purchasedContentPackage = contentPackage;
        this.purchasedContentPackageId = contentPackage != null ? contentPackage.getId() : null;
    }

    public PurchasedContent purchasedContentPackage(ContentPackage contentPackage) {
        this.setPurchasedContentPackage(contentPackage);
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

    public Long getViewerId() {
        return this.viewerId;
    }

    public void setViewerId(Long userProfile) {
        this.viewerId = userProfile;
    }

    public Long getPurchasedContentPackageId() {
        return this.purchasedContentPackageId;
    }

    public void setPurchasedContentPackageId(Long contentPackage) {
        this.purchasedContentPackageId = contentPackage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchasedContent)) {
            return false;
        }
        return getId() != null && getId().equals(((PurchasedContent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchasedContent{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
