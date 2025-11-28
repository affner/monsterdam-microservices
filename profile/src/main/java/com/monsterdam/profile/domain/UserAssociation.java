package com.monsterdam.profile.domain;

import com.monsterdam.profile.domain.enumeration.AssociationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserAssociation.
 */
@Entity
@Table(name = "user_association")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAssociation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "requested_date", nullable = false)
    private Instant requestedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AssociationStatus status;

    @NotNull
    @Column(name = "association_token", nullable = false)
    private String associationToken;

    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

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
    @Column(name = "owner_id", nullable = false)
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

    public Long getOwnerId() {
        return this.ownerId;
    }

    public UserAssociation ownerId(Long ownerId) {
        this.setOwnerId(ownerId);
        return this;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
            ", ownerId=" + getOwnerId() +
            "}";
    }
}
