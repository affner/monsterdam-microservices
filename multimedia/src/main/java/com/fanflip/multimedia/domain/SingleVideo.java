package com.fanflip.multimedia.domain;

import com.fanflip.multimedia.domain.enumeration.MultimediaKind;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A SingleVideo.
 */
@Entity
@DiscriminatorValue(MultimediaKind.VIDEO_ENTITY)
//@Document(indexName = "singlevideo")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SingleVideo extends MultiMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public SingleVideo id(Long id) {
        this.setId(id);
        return this;
    }

    @Override
    public SingleVideo thumbnailContentType(String thumbnailContentType) {
        setThumbnailContentType(thumbnailContentType);
        return this;
    }

    @Override
    public SingleVideo isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    @Override
    public SingleVideo createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    @Override
    public SingleVideo lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return null;
    }

    @Override
    public SingleVideo createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    @Override
    public SingleVideo lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    @Override
    public SingleVideo contentS3Key(String s3Key) {
        this.setContentS3Key(s3Key);
        return this;
    }

    @Override
    public SingleVideo duration(Duration duration) {
        this.setDuration(duration);
        return this;
    }

    @Override
    public SingleVideo belongPackage(ContentPackage contentPackage) {
        this.setBelongPackage(contentPackage);
        return this;
    }

    @Override
    public SingleVideo thumbnailS3Key(String thumbnailS3Key) {
        this.setThumbnailS3Key(thumbnailS3Key);
        return this;
    }

    @Override
    public SingleVideo contentContentType(String contentContentType) {
        this.setContentContentType(contentContentType);
        return this;
    }

    @Override
    public SingleVideo likeCount(Integer likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SingleVideo)) {
            return false;
        }
        return getId() != null && getId().equals(((SingleVideo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SingleVideo{" +
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
