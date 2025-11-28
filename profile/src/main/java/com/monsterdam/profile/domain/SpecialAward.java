package com.monsterdam.profile.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SpecialAward.
 */
@Entity
@Table(name = "special_award")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpecialAward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "reason")
    private String reason;

    @Column(name = "alt_special_title")
    private String altSpecialTitle;

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
    @Column(name = "special_title_id", nullable = false)
    private Long specialTitleId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SpecialAward id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public SpecialAward startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public SpecialAward endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return this.reason;
    }

    public SpecialAward reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAltSpecialTitle() {
        return this.altSpecialTitle;
    }

    public SpecialAward altSpecialTitle(String altSpecialTitle) {
        this.setAltSpecialTitle(altSpecialTitle);
        return this;
    }

    public void setAltSpecialTitle(String altSpecialTitle) {
        this.altSpecialTitle = altSpecialTitle;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public SpecialAward createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public SpecialAward lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public SpecialAward createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public SpecialAward lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public SpecialAward isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getSpecialTitleId() {
        return this.specialTitleId;
    }

    public SpecialAward specialTitleId(Long specialTitleId) {
        this.setSpecialTitleId(specialTitleId);
        return this;
    }

    public void setSpecialTitleId(Long specialTitleId) {
        this.specialTitleId = specialTitleId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecialAward)) {
            return false;
        }
        return getId() != null && getId().equals(((SpecialAward) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecialAward{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", reason='" + getReason() + "'" +
            ", altSpecialTitle='" + getAltSpecialTitle() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", specialTitleId=" + getSpecialTitleId() +
            "}";
    }
}
