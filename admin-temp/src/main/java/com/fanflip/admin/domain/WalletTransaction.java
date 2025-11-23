package com.fanflip.admin.domain;

import com.fanflip.admin.domain.enumeration.WalletTransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A WalletTransaction.
 */
@Table("wallet_transaction")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "wallettransaction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WalletTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("amount")
    private BigDecimal amount;

    @Column("last_modified_date")
    private Instant lastModifiedDate;

    @NotNull(message = "must not be null")
    @Column("transaction_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private WalletTransactionType transactionType;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private Instant createdDate;

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
    private PurchasedContent purchasedContent;

    @Transient
    private PurchasedSubscription purchasedSubscription;

    @Transient
    private PurchasedTip purchasedTip;

    @Column("payment_id")
    private Long paymentId;

    @Column("viewer_id")
    private Long viewerId;

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
        this.amount = amount != null ? amount.stripTrailingZeros() : null;
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

    public PaymentTransaction getPayment() {
        return this.payment;
    }

    public void setPayment(PaymentTransaction paymentTransaction) {
        this.payment = paymentTransaction;
        this.paymentId = paymentTransaction != null ? paymentTransaction.getId() : null;
    }

    public WalletTransaction payment(PaymentTransaction paymentTransaction) {
        this.setPayment(paymentTransaction);
        return this;
    }

    public UserProfile getViewer() {
        return this.viewer;
    }

    public void setViewer(UserProfile userProfile) {
        this.viewer = userProfile;
        this.viewerId = userProfile != null ? userProfile.getId() : null;
    }

    public WalletTransaction viewer(UserProfile userProfile) {
        this.setViewer(userProfile);
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

    public Long getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(Long paymentTransaction) {
        this.paymentId = paymentTransaction;
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
            "}";
    }
}
