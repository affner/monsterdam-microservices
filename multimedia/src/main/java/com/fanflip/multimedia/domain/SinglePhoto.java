package com.fanflip.multimedia.domain;

import com.fanflip.multimedia.domain.enumeration.MultimediaKind;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SinglePhoto.
 */
@Entity
//@Table(name = "single_photo")
@DiscriminatorValue(MultimediaKind.PHOTO_ENTITY)
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SinglePhoto extends MultiMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public SinglePhoto id(Long id) {
        this.setId(id);
        return this;
    }

    @Override
    public SinglePhoto duration(Duration duration) {
        this.setDuration(duration);
        return this;
    }

    @Override
    public SinglePhoto thumbnailContentType(String thumbnailContentType) {
        setThumbnailContentType(thumbnailContentType);
        return this;
    }

    @Override
    public SinglePhoto isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    @Override
    public SinglePhoto createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    @Override
    public SinglePhoto lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return null;
    }

    @Override
    public SinglePhoto createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    @Override
    public SinglePhoto lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    @Override
    public SinglePhoto contentS3Key(String s3Key) {
        this.setContentS3Key(s3Key);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    @Override
    public SinglePhoto belongPackage(ContentPackage contentPackage) {
        this.setBelongPackage(contentPackage);
        return this;
    }

    @Override
    public SinglePhoto thumbnailS3Key(String thumbnailS3Key) {
        this.setThumbnailS3Key(thumbnailS3Key);
        return this;
    }

    @Override
    public SinglePhoto contentContentType(String contentContentType) {
        this.setContentContentType(contentContentType);
        return this;
    }

    @Override
    public SinglePhoto likeCount(Integer likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SinglePhoto)) {
            return false;
        }
        return getId() != null && getId().equals(((SinglePhoto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SinglePhoto{" +
            "id=" + getId() +

            ", thumbnailContentType='" + getThumbnailContentType() + "'" +
            ", thumbnailS3Key='" + getThumbnailS3Key() + "'" +

            ", contentContentType='" + getContentContentType() + "'" +
            ", contentS3Key='" + getContentS3Key() + "'" +
            ", likeCount=" + getLikeCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
