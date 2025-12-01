package com.monsterdam.multimedia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

/**
 * A MultiMedia.
 */
@Entity
@Table(name = "multi_media")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "multimedia_kind", discriminatorType = DiscriminatorType.STRING)
@SuppressWarnings("common-java:DuplicatedBlocks")
public abstract class MultiMedia extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "thumbnail_content_type", nullable = false)
    private String thumbnailContentType;

    @NotNull
    @Column(name = "thumbnail_s_3_key", nullable = false)
    private String thumbnailS3Key;

    @Column(name = "content_content_type")
    private String contentContentType;

    @NotNull
    @Column(name = "content_s_3_key", nullable = false)
    private String contentS3Key;

    @Column(name = "duration")
    private Duration duration;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "like_count")
    private Integer likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "liveStream","audio", "videos", "photos", "message", "post" }, allowSetters = true)
    private ContentPackage belongPackage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public abstract MultiMedia id(Long id);

    public void setId(Long id) {
        this.id = id;
    }

    public String getThumbnailContentType() {
        return this.thumbnailContentType;
    }

    public abstract MultiMedia thumbnailContentType(String thumbnailContentType);

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }


    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public MultiMedia isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public abstract MultiMedia createdDate(Instant createdDate);

    public abstract MultiMedia lastModifiedDate(Instant lastModifiedDate);

    public abstract MultiMedia createdBy(String createdBy);

    public abstract MultiMedia lastModifiedBy(String lastModifiedBy);

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getContentS3Key() {
        return contentS3Key;
    }

    public void setContentS3Key(String contentS3Key) {
        this.contentS3Key = contentS3Key;
    }

    public abstract MultiMedia contentS3Key(String s3Key);

    public Duration getDuration() {
        return this.duration;
    }

    public abstract MultiMedia duration(Duration duration);

    public void setDuration(Duration duration) {
        this.duration = duration;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MultiMedia)) {
            return false;
        }
        return id != null && id.equals(((MultiMedia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    public ContentPackage getBelongPackage() {
        return this.belongPackage;
    }

    public void setBelongPackage(ContentPackage belongPackage) {
        this.belongPackage = belongPackage;
    }

    public abstract MultiMedia belongPackage(ContentPackage contentPackage);

    public String getThumbnailS3Key() {
        return thumbnailS3Key;
    }

    public void setThumbnailS3Key(String thumbnailS3Key) {
        this.thumbnailS3Key = thumbnailS3Key;
    }

    public abstract MultiMedia thumbnailS3Key(String thumbnailS3Key);

    public String getContentContentType() {
        return contentContentType;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public abstract MultiMedia contentContentType(String contentContentType);

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public abstract MultiMedia likeCount(Integer likeCount);

    // prettier-ignore
    @Override
    public String toString() {
        return "MultiMedia{" +
            "id=" + getId() +
            ", thumbnailContentType='" + getThumbnailContentType() + "'" +
            ", thumbnailS3Key='" + getThumbnailS3Key() + "'" +
            ", contentContentType='" + getContentContentType() + "'" +
            ", contentS3Key='" + getContentS3Key() + "'" +
            ", duration='" + getDuration() + "'" +
            ", likeCount=" + getLikeCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
