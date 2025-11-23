package com.fanflip.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SubscriptionBundle.
 */
@Table("subscription_bundle")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "subscriptionbundle")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionBundle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    @DecimalMax(value = "1000")
    @Column("amount")
    private BigDecimal amount;

    @NotNull(message = "must not be null")
    @Column("duration")
    private Duration duration;

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
        value = { "payment", "walletTransaction", "creatorEarning", "subscriptionBundle", "appliedPromotion", "viewer" },
        allowSetters = true
    )
    private Set<PurchasedSubscription> selledSubscriptions = new HashSet<>();

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
    private UserProfile creator;

    @Column("creator_id")
    private Long creatorId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubscriptionBundle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public SubscriptionBundle amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount != null ? amount.stripTrailingZeros() : null;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public SubscriptionBundle duration(Duration duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public SubscriptionBundle createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public SubscriptionBundle lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public SubscriptionBundle createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public SubscriptionBundle lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public SubscriptionBundle isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<PurchasedSubscription> getSelledSubscriptions() {
        return this.selledSubscriptions;
    }

    public void setSelledSubscriptions(Set<PurchasedSubscription> purchasedSubscriptions) {
        if (this.selledSubscriptions != null) {
            this.selledSubscriptions.forEach(i -> i.setSubscriptionBundle(null));
        }
        if (purchasedSubscriptions != null) {
            purchasedSubscriptions.forEach(i -> i.setSubscriptionBundle(this));
        }
        this.selledSubscriptions = purchasedSubscriptions;
    }

    public SubscriptionBundle selledSubscriptions(Set<PurchasedSubscription> purchasedSubscriptions) {
        this.setSelledSubscriptions(purchasedSubscriptions);
        return this;
    }

    public SubscriptionBundle addSelledSubscriptions(PurchasedSubscription purchasedSubscription) {
        this.selledSubscriptions.add(purchasedSubscription);
        purchasedSubscription.setSubscriptionBundle(this);
        return this;
    }

    public SubscriptionBundle removeSelledSubscriptions(PurchasedSubscription purchasedSubscription) {
        this.selledSubscriptions.remove(purchasedSubscription);
        purchasedSubscription.setSubscriptionBundle(null);
        return this;
    }

    public UserProfile getCreator() {
        return this.creator;
    }

    public void setCreator(UserProfile userProfile) {
        this.creator = userProfile;
        this.creatorId = userProfile != null ? userProfile.getId() : null;
    }

    public SubscriptionBundle creator(UserProfile userProfile) {
        this.setCreator(userProfile);
        return this;
    }

    public Long getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Long userProfile) {
        this.creatorId = userProfile;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionBundle)) {
            return false;
        }
        return getId() != null && getId().equals(((SubscriptionBundle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionBundle{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", duration='" + getDuration() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
