package com.fanflip.multimedia.domain;

import com.fanflip.multimedia.domain.enumeration.MultimediaKind;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A VideoStory.
 */
@Entity
@Table(name = "video_story")
//@DiscriminatorValue(MultimediaKind.STORY_ENTITY)
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VideoStory extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @NotNull
    @Column(name = "thumbnail_content_type", nullable = false)
    private String thumbnailContentType;

    @NotNull
    @Column(name = "thumbnail_s_3_key", nullable = false)
    @Field(type = FieldType.Text)
    private String thumbnailS3Key;

    @Column(name = "content_content_type")
    private String contentContentType;

    @NotNull
    @Column(name = "content_s_3_key", nullable = false)
    @Field(type = FieldType.Text)
    private String contentS3Key;

    @Column(name = "duration")
    private Duration duration;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    @Field(type = FieldType.Boolean)
    private Boolean isDeleted = false;

    @Column(name = "like_count")
    @Field(type = FieldType.Integer)
    private Integer likeCount;


    // jhipster-needle-entity-add-field - JHipster will add fields here
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return this.id;
    }


    public Long getCreatorId() {
        return this.creatorId;
    }
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getThumbnailContentType() {
        return this.thumbnailContentType;
    }


    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }
    public String getContentContentType() {
        return contentContentType;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }
    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
    public String getThumbnailS3Key() {
        return thumbnailS3Key;
    }

    public void setThumbnailS3Key(String thumbnailS3Key) {
        this.thumbnailS3Key = thumbnailS3Key;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getContentS3Key() {
        return contentS3Key;
    }

    public void setContentS3Key(String contentS3Key) {
        this.contentS3Key = contentS3Key;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }



    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VideoStory)) {
            return false;
        }
        return getId() != null && getId().equals(((VideoStory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    public VideoStory creatorId(Long creatorId) {
        this.setCreatorId(creatorId);
        return this;
    }

    public VideoStory id(Long id) {
        this.setId(id);
        return this;
    }


    public VideoStory thumbnailContentType(String thumbnailContentType) {
        setThumbnailContentType(thumbnailContentType);
        return this;
    }


    public VideoStory isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }


    public VideoStory createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }


    public VideoStory lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }


    public VideoStory createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }


    public VideoStory lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }


    public VideoStory contentS3Key(String s3Key) {
        this.setContentS3Key(s3Key);
        return null;
    }


    public VideoStory duration(Duration duration) {
        this.setDuration(duration);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here



    public VideoStory thumbnailS3Key(String thumbnailS3Key) {
        this.setThumbnailS3Key(thumbnailS3Key);
        return this;
    }


    public VideoStory contentContentType(String contentContentType) {
        this.setContentContentType(contentContentType);
        return this;
    }


    public VideoStory likeCount(Integer likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VideoStory{" +
            "id=" + getId() +
            ", creatorId='" + getCreatorId() + "'" +
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
