package com.fanflip.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SingleLiveStream.
 */
@Table("single_live_stream")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "singlelivestream")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SingleLiveStream implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("title")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @Column("description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column("thumbnail")
    private byte[] thumbnail;

    @Column("thumbnail_content_type")
    private String thumbnailContentType;

    @Column("thumbnail_s_3_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String thumbnailS3Key;

    @NotNull(message = "must not be null")
    @Column("start_time")
    private Instant startTime;

    @Column("end_time")
    private Instant endTime;

    @Column("live_content")
    private byte[] liveContent;

    @Column("live_content_content_type")
    private String liveContentContentType;

    @Column("live_content_s_3_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String liveContentS3Key;

    @NotNull(message = "must not be null")
    @Column("is_recorded")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isRecorded;

    @Column("like_count")
    private Long likeCount;

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

    @Transient
    @JsonIgnoreProperties(
        value = { "ticket", "reporter", "reported", "story", "video", "photo", "audio", "liveStream", "message", "post", "postComment" },
        allowSetters = true
    )
    private Set<UserReport> reports = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SingleLiveStream id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public SingleLiveStream title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public SingleLiveStream description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getThumbnail() {
        return this.thumbnail;
    }

    public SingleLiveStream thumbnail(byte[] thumbnail) {
        this.setThumbnail(thumbnail);
        return this;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return this.thumbnailContentType;
    }

    public SingleLiveStream thumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
        return this;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public String getThumbnailS3Key() {
        return this.thumbnailS3Key;
    }

    public SingleLiveStream thumbnailS3Key(String thumbnailS3Key) {
        this.setThumbnailS3Key(thumbnailS3Key);
        return this;
    }

    public void setThumbnailS3Key(String thumbnailS3Key) {
        this.thumbnailS3Key = thumbnailS3Key;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public SingleLiveStream startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public SingleLiveStream endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public byte[] getLiveContent() {
        return this.liveContent;
    }

    public SingleLiveStream liveContent(byte[] liveContent) {
        this.setLiveContent(liveContent);
        return this;
    }

    public void setLiveContent(byte[] liveContent) {
        this.liveContent = liveContent;
    }

    public String getLiveContentContentType() {
        return this.liveContentContentType;
    }

    public SingleLiveStream liveContentContentType(String liveContentContentType) {
        this.liveContentContentType = liveContentContentType;
        return this;
    }

    public void setLiveContentContentType(String liveContentContentType) {
        this.liveContentContentType = liveContentContentType;
    }

    public String getLiveContentS3Key() {
        return this.liveContentS3Key;
    }

    public SingleLiveStream liveContentS3Key(String liveContentS3Key) {
        this.setLiveContentS3Key(liveContentS3Key);
        return this;
    }

    public void setLiveContentS3Key(String liveContentS3Key) {
        this.liveContentS3Key = liveContentS3Key;
    }

    public Boolean getIsRecorded() {
        return this.isRecorded;
    }

    public SingleLiveStream isRecorded(Boolean isRecorded) {
        this.setIsRecorded(isRecorded);
        return this;
    }

    public void setIsRecorded(Boolean isRecorded) {
        this.isRecorded = isRecorded;
    }

    public Long getLikeCount() {
        return this.likeCount;
    }

    public SingleLiveStream likeCount(Long likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public SingleLiveStream createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public SingleLiveStream lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public SingleLiveStream createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public SingleLiveStream lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public SingleLiveStream isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<UserReport> getReports() {
        return this.reports;
    }

    public void setReports(Set<UserReport> userReports) {
        if (this.reports != null) {
            this.reports.forEach(i -> i.setLiveStream(null));
        }
        if (userReports != null) {
            userReports.forEach(i -> i.setLiveStream(this));
        }
        this.reports = userReports;
    }

    public SingleLiveStream reports(Set<UserReport> userReports) {
        this.setReports(userReports);
        return this;
    }

    public SingleLiveStream addReports(UserReport userReport) {
        this.reports.add(userReport);
        userReport.setLiveStream(this);
        return this;
    }

    public SingleLiveStream removeReports(UserReport userReport) {
        this.reports.remove(userReport);
        userReport.setLiveStream(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SingleLiveStream{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", thumbnail='" + getThumbnail() + "'" +
            ", thumbnailContentType='" + getThumbnailContentType() + "'" +
            ", thumbnailS3Key='" + getThumbnailS3Key() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", liveContent='" + getLiveContent() + "'" +
            ", liveContentContentType='" + getLiveContentContentType() + "'" +
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
