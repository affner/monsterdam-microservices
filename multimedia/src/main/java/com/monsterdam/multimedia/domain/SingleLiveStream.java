package com.monsterdam.multimedia.domain;

import com.monsterdam.multimedia.domain.enumeration.MultimediaKind;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

/**
 * A SingleLiveStream.
 */
@Entity
//@Table(name = "single_live_stream")
@DiscriminatorValue(MultimediaKind.LIVESTREAM_ENTITY)
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SingleLiveStream extends MultiMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnoreProperties(value = { "liveStream","audio", "videos", "photos", "message", "post" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "liveStream")
    @org.springframework.data.annotation.Transient
    private ContentPackage contentPackage;

    // jhipster-needle-entity-add-field - JHipster will add fields here



    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
    @Override
    public SingleLiveStream id(Long id) {
        this.setId(id);
        return this;
    }

    @Override
    public SingleLiveStream thumbnailContentType(String thumbnailContentType) {
        setThumbnailContentType(thumbnailContentType);
        return this;
    }

    @Override
    public SingleLiveStream isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    @Override
    public SingleLiveStream createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    @Override
    public SingleLiveStream lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return null;
    }

    @Override
    public SingleLiveStream createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    @Override
    public SingleLiveStream lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    @Override
    public SingleLiveStream contentS3Key(String s3Key) {
        this.setContentS3Key(s3Key);
        return this;
    }

    @Override
    public SingleLiveStream duration(Duration duration) {
        this.setDuration(duration);
        return this;
    }

    public ContentPackage getContentPackage() {
        return this.contentPackage;
    }

    public void setContentPackage(ContentPackage contentPackage) {
        if (this.contentPackage != null) {
            this.contentPackage.setLiveStream(null);
        }
        if (contentPackage != null) {
            contentPackage.setLiveStream(this);
        }
        this.contentPackage = contentPackage;
    }

    public SingleLiveStream contentPackage(ContentPackage postMultimediaPackage) {
        this.setContentPackage(postMultimediaPackage);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public SingleLiveStream belongPackage(ContentPackage contentPackage) {
        return this;
    }

    @Override
    public SingleLiveStream thumbnailS3Key(String thumbnailS3Key) {
        this.setThumbnailS3Key(thumbnailS3Key);
        return this;
    }

    @Override
    public SingleLiveStream contentContentType(String contentContentType) {
        this.setContentContentType(contentContentType);
        return this;
    }

    @Override
    public SingleLiveStream likeCount(Integer likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SingleLiveStream{" +
            "id=" + getId() +
            ", thumbnailContentType='" + getThumbnailContentType() + "'" +
            ", thumbnailS3Key='" + getThumbnailS3Key() + "'" +
            ", contentContentType='" + getContentContentType() + "'" +
            ", contentS3Key='" + getContentS3Key() + "'" +
            ", duration='" + getDuration() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SingleLiveStream)) {
            return false;
        }
        return getId() != null && getId().equals(((SingleLiveStream) o).getId());
    }


}
