package com.monsterdam.finance.domain;

import com.monsterdam.finance.domain.enumeration.OfferPromotionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OfferPromotion.
 */
@Entity
@Table(name = "offer_promotion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OfferPromotion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "free_days_duration")
    private Duration freeDaysDuration;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "discount_percentage")
    private Float discountPercentage;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "subscriptions_limit")
    private Integer subscriptionsLimit;

    @NotNull
    @Column(name = "link_code", nullable = false)
    private String linkCode;

    @NotNull
    @Column(name = "is_finished", nullable = false)
    private Boolean isFinished;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_type", nullable = false)
    private OfferPromotionType promotionType;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appliedPromotion")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "payment", "walletTransaction", "creatorEarning", "subscriptionBundle", "appliedPromotion" },
        allowSetters = true
    )
    private Set<PurchasedSubscription> purchasedSubscriptions = new HashSet<>();

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
