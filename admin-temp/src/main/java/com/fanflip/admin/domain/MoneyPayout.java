package com.monsterdam.admin.domain;

import com.monsterdam.admin.domain.enumeration.PayoutStatus;
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
 * A MoneyPayout.
 */
@Table("money_payout")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "moneypayout")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyPayout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("amount")
    private BigDecimal amount;

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
    @Column("withdraw_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private PayoutStatus withdrawStatus;

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
    private UserProfile creator;

    @Column("creator_earning_id")
    private Long creatorEarningId;

    @Column("creator_id")
    private Long creatorId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MoneyPayout id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public MoneyPayout amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount != null ? amount.stripTrailingZeros() : null;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public MoneyPayout createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public MoneyPayout lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public MoneyPayout createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public MoneyPayout lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public MoneyPayout isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public PayoutStatus getWithdrawStatus() {
        return this.withdrawStatus;
    }

    public MoneyPayout withdrawStatus(PayoutStatus withdrawStatus) {
        this.setWithdrawStatus(withdrawStatus);
        return this;
    }

    public void setWithdrawStatus(PayoutStatus withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public CreatorEarning getCreatorEarning() {
        return this.creatorEarning;
    }

    public void setCreatorEarning(CreatorEarning creatorEarning) {
        this.creatorEarning = creatorEarning;
        this.creatorEarningId = creatorEarning != null ? creatorEarning.getId() : null;
    }

    public MoneyPayout creatorEarning(CreatorEarning creatorEarning) {
        this.setCreatorEarning(creatorEarning);
        return this;
    }

    public UserProfile getCreator() {
        return this.creator;
    }

    public void setCreator(UserProfile userProfile) {
        this.creator = userProfile;
        this.creatorId = userProfile != null ? userProfile.getId() : null;
    }

    public MoneyPayout creator(UserProfile userProfile) {
        this.setCreator(userProfile);
        return this;
    }

    public Long getCreatorEarningId() {
        return this.creatorEarningId;
    }

    public void setCreatorEarningId(Long creatorEarning) {
        this.creatorEarningId = creatorEarning;
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
        if (!(o instanceof MoneyPayout)) {
            return false;
        }
        return getId() != null && getId().equals(((MoneyPayout) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyPayout{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", withdrawStatus='" + getWithdrawStatus() + "'" +
            "}";
    }
}
