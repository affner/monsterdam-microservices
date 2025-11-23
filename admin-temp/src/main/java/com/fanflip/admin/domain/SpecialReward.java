package com.fanflip.admin.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SpecialReward.
 */
@Table("special_reward")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "specialreward")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpecialReward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

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
    @Column("content_package_id")
    private Long contentPackageId;

    @NotNull(message = "must not be null")
    @Column("viewer_id")
    private Long viewerId;

    @NotNull(message = "must not be null")
    @Column("offer_promotion_id")
    private Long offerPromotionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SpecialReward id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public SpecialReward description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public SpecialReward createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public SpecialReward lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public SpecialReward createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public SpecialReward lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public SpecialReward isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getContentPackageId() {
        return this.contentPackageId;
    }

    public SpecialReward contentPackageId(Long contentPackageId) {
        this.setContentPackageId(contentPackageId);
        return this;
    }

    public void setContentPackageId(Long contentPackageId) {
        this.contentPackageId = contentPackageId;
    }

    public Long getViewerId() {
        return this.viewerId;
    }

    public SpecialReward viewerId(Long viewerId) {
        this.setViewerId(viewerId);
        return this;
    }

    public void setViewerId(Long viewerId) {
        this.viewerId = viewerId;
    }

    public Long getOfferPromotionId() {
        return this.offerPromotionId;
    }

    public SpecialReward offerPromotionId(Long offerPromotionId) {
        this.setOfferPromotionId(offerPromotionId);
        return this;
    }

    public void setOfferPromotionId(Long offerPromotionId) {
        this.offerPromotionId = offerPromotionId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecialReward)) {
            return false;
        }
        return getId() != null && getId().equals(((SpecialReward) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecialReward{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", contentPackageId=" + getContentPackageId() +
            ", viewerId=" + getViewerId() +
            ", offerPromotionId=" + getOfferPromotionId() +
            "}";
    }
}
