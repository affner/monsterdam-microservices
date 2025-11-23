package com.fanflip.admin.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.SingleLiveStream} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SingleLiveStreamDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    @Lob
    private byte[] thumbnail;

    private String thumbnailContentType;
    private String thumbnailS3Key;

    @NotNull(message = "must not be null")
    private Instant startTime;

    private Instant endTime;

    @Lob
    private byte[] liveContent;

    private String liveContentContentType;
    private String liveContentS3Key;

    @NotNull(message = "must not be null")
    private Boolean isRecorded;

    private Long likeCount;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return thumbnailContentType;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public String getThumbnailS3Key() {
        return thumbnailS3Key;
    }

    public void setThumbnailS3Key(String thumbnailS3Key) {
        this.thumbnailS3Key = thumbnailS3Key;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public byte[] getLiveContent() {
        return liveContent;
    }

    public void setLiveContent(byte[] liveContent) {
        this.liveContent = liveContent;
    }

    public String getLiveContentContentType() {
        return liveContentContentType;
    }

    public void setLiveContentContentType(String liveContentContentType) {
        this.liveContentContentType = liveContentContentType;
    }

    public String getLiveContentS3Key() {
        return liveContentS3Key;
    }

    public void setLiveContentS3Key(String liveContentS3Key) {
        this.liveContentS3Key = liveContentS3Key;
    }

    public Boolean getIsRecorded() {
        return isRecorded;
    }

    public void setIsRecorded(Boolean isRecorded) {
        this.isRecorded = isRecorded;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SingleLiveStreamDTO)) {
            return false;
        }

        SingleLiveStreamDTO singleLiveStreamDTO = (SingleLiveStreamDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, singleLiveStreamDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SingleLiveStreamDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", thumbnail='" + getThumbnail() + "'" +
            ", thumbnailS3Key='" + getThumbnailS3Key() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", liveContent='" + getLiveContent() + "'" +
            ", liveContentS3Key='" + getLiveContentS3Key() + "'" +
            ", isRecorded='" + getIsRecorded() + "'" +
            ", likeCount=" + getLikeCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
