package com.fanflip.finance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubscriptionBundle.
 */
@Entity
@Table(name = "subscription_bundle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionBundle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1000")
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Duration duration;

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
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscriptionBundle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "payment", "walletTransaction", "creatorEarning", "subscriptionBundle", "appliedPromotion" },
        allowSetters = true
    )
    private Set<PurchasedSubscription> selledSubscriptions = new HashSet<>();

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
        this.amount = amount;
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

    public Long getCreatorId() {
        return this.creatorId;
    }

    public SubscriptionBundle creatorId(Long creatorId) {
        this.setCreatorId(creatorId);
        return this;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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
            ", creatorId=" + getCreatorId() +
            "}";
    }
}
