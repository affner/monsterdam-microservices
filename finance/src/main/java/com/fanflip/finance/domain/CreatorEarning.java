package com.fanflip.finance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CreatorEarning.
 */
@Entity
@Table(name = "creator_earning")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CreatorEarning implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

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

    @JsonIgnoreProperties(value = { "creatorEarning" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "creatorEarning")
    private MoneyPayout moneyPayout;

    @JsonIgnoreProperties(value = { "payment", "walletTransaction", "creatorEarning" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "creatorEarning")
    private PurchasedTip purchasedTip;

    @JsonIgnoreProperties(value = { "payment", "walletTransaction", "creatorEarning" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "creatorEarning")
    private PurchasedContent purchasedContent;

    @JsonIgnoreProperties(
        value = { "payment", "walletTransaction", "creatorEarning", "subscriptionBundle", "appliedPromotion" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "creatorEarning")
    private PurchasedSubscription purchasedSubscription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CreatorEarning id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public CreatorEarning amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public CreatorEarning createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public CreatorEarning lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public CreatorEarning createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public CreatorEarning lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public CreatorEarning isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getCreatorId() {
        return this.creatorId;
    }

    public CreatorEarning creatorId(Long creatorId) {
        this.setCreatorId(creatorId);
        return this;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public MoneyPayout getMoneyPayout() {
        return this.moneyPayout;
    }

    public void setMoneyPayout(MoneyPayout moneyPayout) {
        if (this.moneyPayout != null) {
            this.moneyPayout.setCreatorEarning(null);
        }
        if (moneyPayout != null) {
            moneyPayout.setCreatorEarning(this);
        }
        this.moneyPayout = moneyPayout;
    }

    public CreatorEarning moneyPayout(MoneyPayout moneyPayout) {
        this.setMoneyPayout(moneyPayout);
        return this;
    }

    public PurchasedTip getPurchasedTip() {
        return this.purchasedTip;
    }

    public void setPurchasedTip(PurchasedTip purchasedTip) {
        if (this.purchasedTip != null) {
            this.purchasedTip.setCreatorEarning(null);
        }
        if (purchasedTip != null) {
            purchasedTip.setCreatorEarning(this);
        }
        this.purchasedTip = purchasedTip;
    }

    public CreatorEarning purchasedTip(PurchasedTip purchasedTip) {
        this.setPurchasedTip(purchasedTip);
        return this;
    }

    public PurchasedContent getPurchasedContent() {
        return this.purchasedContent;
    }

    public void setPurchasedContent(PurchasedContent purchasedContent) {
        if (this.purchasedContent != null) {
            this.purchasedContent.setCreatorEarning(null);
        }
        if (purchasedContent != null) {
            purchasedContent.setCreatorEarning(this);
        }
        this.purchasedContent = purchasedContent;
    }

    public CreatorEarning purchasedContent(PurchasedContent purchasedContent) {
        this.setPurchasedContent(purchasedContent);
        return this;
    }

    public PurchasedSubscription getPurchasedSubscription() {
        return this.purchasedSubscription;
    }

    public void setPurchasedSubscription(PurchasedSubscription purchasedSubscription) {
        if (this.purchasedSubscription != null) {
            this.purchasedSubscription.setCreatorEarning(null);
        }
        if (purchasedSubscription != null) {
            purchasedSubscription.setCreatorEarning(this);
        }
        this.purchasedSubscription = purchasedSubscription;
    }

    public CreatorEarning purchasedSubscription(PurchasedSubscription purchasedSubscription) {
        this.setPurchasedSubscription(purchasedSubscription);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreatorEarning)) {
            return false;
        }
        return getId() != null && getId().equals(((CreatorEarning) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CreatorEarning{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", creatorId=" + getCreatorId() +
            "}";
    }
}
