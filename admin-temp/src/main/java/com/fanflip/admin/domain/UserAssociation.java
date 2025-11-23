package com.fanflip.admin.domain;

import com.fanflip.admin.domain.enumeration.AssociationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UserAssociation.
 */
@Table("user_association")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "userassociation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAssociation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("requested_date")
    private Instant requestedDate;

    @Column("status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AssociationStatus status;

    @NotNull(message = "must not be null")
    @Column("association_token")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String associationToken;

    @NotNull(message = "must not be null")
    @Column("expiry_date")
    private Instant expiryDate;

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
    private UserProfile owner;

    @Column("owner_id")
    private Long ownerId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAssociation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getRequestedDate() {
        return this.requestedDate;
    }

    public UserAssociation requestedDate(Instant requestedDate) {
        this.setRequestedDate(requestedDate);
        return this;
    }

    public void setRequestedDate(Instant requestedDate) {
        this.requestedDate = requestedDate;
    }

    public AssociationStatus getStatus() {
        return this.status;
    }

    public UserAssociation status(AssociationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AssociationStatus status) {
        this.status = status;
    }

    public String getAssociationToken() {
        return this.associationToken;
    }

    public UserAssociation associationToken(String associationToken) {
        this.setAssociationToken(associationToken);
        return this;
    }

    public void setAssociationToken(String associationToken) {
        this.associationToken = associationToken;
    }

    public Instant getExpiryDate() {
        return this.expiryDate;
    }

    public UserAssociation expiryDate(Instant expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserAssociation createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserAssociation lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserAssociation createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserAssociation lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public UserAssociation isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public UserProfile getOwner() {
        return this.owner;
    }

    public void setOwner(UserProfile userProfile) {
        this.owner = userProfile;
        this.ownerId = userProfile != null ? userProfile.getId() : null;
    }

    public UserAssociation owner(UserProfile userProfile) {
        this.setOwner(userProfile);
        return this;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long userProfile) {
        this.ownerId = userProfile;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAssociation)) {
            return false;
        }
        return getId() != null && getId().equals(((UserAssociation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAssociation{" +
            "id=" + getId() +
            ", requestedDate='" + getRequestedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", associationToken='" + getAssociationToken() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
