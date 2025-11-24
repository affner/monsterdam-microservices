package com.monsterdam.admin.domain;

import com.monsterdam.admin.domain.enumeration.OfferPromotionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A OfferPromotion.
 */
@Table("offer_promotion")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "offerpromotion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OfferPromotion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("free_days_duration")
    private Duration freeDaysDuration;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column("discount_percentage")
    private Float discountPercentage;

    @NotNull(message = "must not be null")
    @Column("start_date")
    private LocalDate startDate;

    @NotNull(message = "must not be null")
    @Column("end_date")
    private LocalDate endDate;

    @Column("subscriptions_limit")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer subscriptionsLimit;

    @NotNull(message = "must not be null")
    @Column("link_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String linkCode;

    @NotNull(message = "must not be null")
    @Column("is_finished")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isFinished;

    @NotNull(message = "must not be null")
    @Column("promotion_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private OfferPromotionType promotionType;

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
    private Set<PurchasedSubscription> purchasedSubscriptions = new HashSet<>();

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

    public OfferPromotion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Duration getFreeDaysDuration() {
        return this.freeDaysDuration;
    }

    public OfferPromotion freeDaysDuration(Duration freeDaysDuration) {
        this.setFreeDaysDuration(freeDaysDuration);
        return this;
    }

    public void setFreeDaysDuration(Duration freeDaysDuration) {
        this.freeDaysDuration = freeDaysDuration;
    }

    public Float getDiscountPercentage() {
        return this.discountPercentage;
    }

    public OfferPromotion discountPercentage(Float discountPercentage) {
        this.setDiscountPercentage(discountPercentage);
        return this;
    }

    public void setDiscountPercentage(Float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public OfferPromotion startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public OfferPromotion endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getSubscriptionsLimit() {
        return this.subscriptionsLimit;
    }

    public OfferPromotion subscriptionsLimit(Integer subscriptionsLimit) {
        this.setSubscriptionsLimit(subscriptionsLimit);
        return this;
    }

    public void setSubscriptionsLimit(Integer subscriptionsLimit) {
        this.subscriptionsLimit = subscriptionsLimit;
    }

    public String getLinkCode() {
        return this.linkCode;
    }

    public OfferPromotion linkCode(String linkCode) {
        this.setLinkCode(linkCode);
        return this;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }

    public Boolean getIsFinished() {
        return this.isFinished;
    }

    public OfferPromotion isFinished(Boolean isFinished) {
        this.setIsFinished(isFinished);
        return this;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public OfferPromotionType getPromotionType() {
        return this.promotionType;
    }

    public OfferPromotion promotionType(OfferPromotionType promotionType) {
        this.setPromotionType(promotionType);
        return this;
    }

    public void setPromotionType(OfferPromotionType promotionType) {
        this.promotionType = promotionType;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public OfferPromotion createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public OfferPromotion lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public OfferPromotion createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public OfferPromotion lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public OfferPromotion isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<PurchasedSubscription> getPurchasedSubscriptions() {
        return this.purchasedSubscriptions;
    }

    public void setPurchasedSubscriptions(Set<PurchasedSubscription> purchasedSubscriptions) {
        if (this.purchasedSubscriptions != null) {
            this.purchasedSubscriptions.forEach(i -> i.setAppliedPromotion(null));
        }
        if (purchasedSubscriptions != null) {
            purchasedSubscriptions.forEach(i -> i.setAppliedPromotion(this));
        }
        this.purchasedSubscriptions = purchasedSubscriptions;
    }

    public OfferPromotion purchasedSubscriptions(Set<PurchasedSubscription> purchasedSubscriptions) {
        this.setPurchasedSubscriptions(purchasedSubscriptions);
        return this;
    }

    public OfferPromotion addPurchasedSubscriptions(PurchasedSubscription purchasedSubscription) {
        this.purchasedSubscriptions.add(purchasedSubscription);
        purchasedSubscription.setAppliedPromotion(this);
        return this;
    }

    public OfferPromotion removePurchasedSubscriptions(PurchasedSubscription purchasedSubscription) {
        this.purchasedSubscriptions.remove(purchasedSubscription);
        purchasedSubscription.setAppliedPromotion(null);
        return this;
    }

    public UserProfile getCreator() {
        return this.creator;
    }

    public void setCreator(UserProfile userProfile) {
        this.creator = userProfile;
        this.creatorId = userProfile != null ? userProfile.getId() : null;
    }

    public OfferPromotion creator(UserProfile userProfile) {
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
        if (!(o instanceof OfferPromotion)) {
            return false;
        }
        return getId() != null && getId().equals(((OfferPromotion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OfferPromotion{" +
            "id=" + getId() +
            ", freeDaysDuration='" + getFreeDaysDuration() + "'" +
            ", discountPercentage=" + getDiscountPercentage() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", subscriptionsLimit=" + getSubscriptionsLimit() +
            ", linkCode='" + getLinkCode() + "'" +
            ", isFinished='" + getIsFinished() + "'" +
            ", promotionType='" + getPromotionType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
