package com.monsterdam.multimedia.domain;

import com.monsterdam.multimedia.domain.enumeration.TagStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserTagRelation.
 */
@Entity
@Table(name = "rel_user_tag_relation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserTagRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

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
    @Column(name = "tagged_user_id", nullable = false)
    private Long taggedUserId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tag_status", nullable = false)
    private TagStatus tagStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "audio", "videos", "photos", "tags", "specialReward" }, allowSetters = true)
    private ContentPackage contentPackage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserTagRelation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserTagRelation createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserTagRelation lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserTagRelation createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserTagRelation lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public UserTagRelation isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getTaggedUserId() {
        return this.taggedUserId;
    }

    public UserTagRelation taggedUserId(Long taggedUserId) {
        this.setTaggedUserId(taggedUserId);
        return this;
    }

    public void setTaggedUserId(Long taggedUserId) {
        this.taggedUserId = taggedUserId;
    }

    public TagStatus getTagStatus() {
        return this.tagStatus;
    }

    public UserTagRelation tagStatus(TagStatus tagStatus) {
        this.setTagStatus(tagStatus);
        return this;
    }

    public void setTagStatus(TagStatus tagStatus) {
        this.tagStatus = tagStatus;
    }

    public ContentPackage getContentPackage() {
        return this.contentPackage;
    }

    public void setContentPackage(ContentPackage contentPackage) {
        this.contentPackage = contentPackage;
    }

    public UserTagRelation contentPackage(ContentPackage contentPackage) {
        this.setContentPackage(contentPackage);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserTagRelation)) {
            return false;
        }
        return getId() != null && getId().equals(((UserTagRelation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserTagRelation{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", taggedUserId=" + getTaggedUserId() +
            ", tagStatus='" + getTagStatus() + "'" +
            "}";
    }
}
