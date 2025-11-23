package com.fanflip.admin.domain;

import com.fanflip.admin.domain.enumeration.GenericStatus;
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
 * A PaymentTransaction.
 */
@Table("payment_transaction")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "paymenttransaction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("amount")
    private BigDecimal amount;

    @NotNull(message = "must not be null")
    @Column("payment_date")
    private Instant paymentDate;

    @NotNull(message = "must not be null")
    @Column("payment_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private GenericStatus paymentStatus;

    @Size(max = 100)
    @Column("payment_reference")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String paymentReference;

    @Size(max = 100)
    @Column("cloud_transaction_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cloudTransactionId;

    @Transient
    @JsonIgnoreProperties(value = { "payments" }, allowSetters = true)
    private PaymentMethod paymentMethod;

    @Transient
    @JsonIgnoreProperties(value = { "payments" }, allowSetters = true)
    private PaymentProvider paymentProvider;

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
    private AccountingRecord accountingRecord;

    @Transient
    private PurchasedContent purchasedContent;

    @Transient
    private PurchasedSubscription purchasedSubscription;

    @Transient
    private WalletTransaction walletTransaction;

    @Transient
    private PurchasedTip purchasedTip;

    @Column("payment_method_id")
    private Long paymentMethodId;

    @Column("payment_provider_id")
    private Long paymentProviderId;

    @Column("viewer_id")
    private Long viewerId;

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
        this.amount = amount != null ? amount.stripTrailingZeros() : null;
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

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.paymentMethodId = paymentMethod != null ? paymentMethod.getId() : null;
    }

    public PaymentTransaction paymentMethod(PaymentMethod paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public PaymentProvider getPaymentProvider() {
        return this.paymentProvider;
    }

    public void setPaymentProvider(PaymentProvider paymentProvider) {
        this.paymentProvider = paymentProvider;
        this.paymentProviderId = paymentProvider != null ? paymentProvider.getId() : null;
    }

    public PaymentTransaction paymentProvider(PaymentProvider paymentProvider) {
        this.setPaymentProvider(paymentProvider);
        return this;
    }

    public UserProfile getViewer() {
        return this.viewer;
    }

    public void setViewer(UserProfile userProfile) {
        this.viewer = userProfile;
        this.viewerId = userProfile != null ? userProfile.getId() : null;
    }

    public PaymentTransaction viewer(UserProfile userProfile) {
        this.setViewer(userProfile);
        return this;
    }

    public AccountingRecord getAccountingRecord() {
        return this.accountingRecord;
    }

    public void setAccountingRecord(AccountingRecord accountingRecord) {
        if (this.accountingRecord != null) {
            this.accountingRecord.setPayment(null);
        }
        if (accountingRecord != null) {
            accountingRecord.setPayment(this);
        }
        this.accountingRecord = accountingRecord;
    }

    public PaymentTransaction accountingRecord(AccountingRecord accountingRecord) {
        this.setAccountingRecord(accountingRecord);
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

    public Long getPaymentMethodId() {
        return this.paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethod) {
        this.paymentMethodId = paymentMethod;
    }

    public Long getPaymentProviderId() {
        return this.paymentProviderId;
    }

    public void setPaymentProviderId(Long paymentProvider) {
        this.paymentProviderId = paymentProvider;
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
            "}";
    }
}
