package com.monsterdam.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A VideoStory.
 */
@Table("video_story")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "videostory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VideoStory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("thumbnail")
    private byte[] thumbnail;

    @NotNull
    @Column("thumbnail_content_type")
    private String thumbnailContentType;

    @NotNull(message = "must not be null")
    @Column("thumbnail_s_3_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String thumbnailS3Key;

    @Column("content")
    private byte[] content;

    @Column("content_content_type")
    private String contentContentType;

    @NotNull(message = "must not be null")
    @Column("content_s_3_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String contentS3Key;

    @Column("duration")
    private Duration duration;

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

    @Transient
    @JsonIgnoreProperties(
        value = {
            "contentPackage",
            "reports",
            "responses",
            "responseTo",
            "repliedStory",
            "user",
            "bookMarks",
            "chatRooms",
            "adminAnnouncement",
            "purchasedTip",
        },
        allowSetters = true
    )
    private Set<DirectMessage> messages = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = {
            "userLite",
            "settings",
            "makedReports",
            "receivedReports",
            "assistanceTickets",
            "withdraws",
            "subscriptionBundles",
            "earnings",
            "payments",
            "planOffers",
            "purchasedContents",
            "purchasedSubscriptions",
            "wallets",
            "socialNetworks",
            "commentNotifications",
            "messageNotifications",
            "userMentionNotifications",
            "commentMentionNotifications",
            "ownAccountsAssociations",
            "createdEvents",
            "bookMarks",
            "feedbacks",
            "sentMessages",
            "chats",
            "mentions",
            "comments",
            "feeds",
            "votedPolls",
            "videoStories",
            "documents",
            "countryOfBirth",
            "stateOfResidence",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "blockedUbications",
            "hashTags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
            "contentPackageTags",
        },
        allowSetters = true
    )
    private UserProfile creator;

    @Column("creator_id")
    private Long creatorId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VideoStory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getThumbnail() {
        return this.thumbnail;
    }

    public VideoStory thumbnail(byte[] thumbnail) {
        this.setThumbnail(thumbnail);
        return this;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return this.thumbnailContentType;
    }

    public VideoStory thumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
        return this;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public String getThumbnailS3Key() {
        return this.thumbnailS3Key;
    }

    public VideoStory thumbnailS3Key(String thumbnailS3Key) {
        this.setThumbnailS3Key(thumbnailS3Key);
        return this;
    }

    public void setThumbnailS3Key(String thumbnailS3Key) {
        this.thumbnailS3Key = thumbnailS3Key;
    }

    public byte[] getContent() {
        return this.content;
    }

    public VideoStory content(byte[] content) {
        this.setContent(content);
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return this.contentContentType;
    }

    public VideoStory contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public String getContentS3Key() {
        return this.contentS3Key;
    }

    public VideoStory contentS3Key(String contentS3Key) {
        this.setContentS3Key(contentS3Key);
        return this;
    }

    public void setContentS3Key(String contentS3Key) {
        this.contentS3Key = contentS3Key;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public VideoStory duration(Duration duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Long getLikeCount() {
        return this.likeCount;
    }

    public VideoStory likeCount(Long likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public VideoStory createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public VideoStory lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public VideoStory createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public VideoStory lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public VideoStory isDeleted(Boolean isDeleted) {
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
            this.reports.forEach(i -> i.setStory(null));
        }
        if (userReports != null) {
            userReports.forEach(i -> i.setStory(this));
        }
        this.reports = userReports;
    }

    public VideoStory reports(Set<UserReport> userReports) {
        this.setReports(userReports);
        return this;
    }

    public VideoStory addReports(UserReport userReport) {
        this.reports.add(userReport);
        userReport.setStory(this);
        return this;
    }

    public VideoStory removeReports(UserReport userReport) {
        this.reports.remove(userReport);
        userReport.setStory(null);
        return this;
    }

    public Set<DirectMessage> getMessages() {
        return this.messages;
    }

    public void setMessages(Set<DirectMessage> directMessages) {
        if (this.messages != null) {
            this.messages.forEach(i -> i.setRepliedStory(null));
        }
        if (directMessages != null) {
            directMessages.forEach(i -> i.setRepliedStory(this));
        }
        this.messages = directMessages;
    }

    public VideoStory messages(Set<DirectMessage> directMessages) {
        this.setMessages(directMessages);
        return this;
    }

    public VideoStory addMessages(DirectMessage directMessage) {
        this.messages.add(directMessage);
        directMessage.setRepliedStory(this);
        return this;
    }

    public VideoStory removeMessages(DirectMessage directMessage) {
        this.messages.remove(directMessage);
        directMessage.setRepliedStory(null);
        return this;
    }

    public UserProfile getCreator() {
        return this.creator;
    }

    public void setCreator(UserProfile userProfile) {
        this.creator = userProfile;
        this.creatorId = userProfile != null ? userProfile.getId() : null;
    }

    public VideoStory creator(UserProfile userProfile) {
        this.setCreator(userProfile);
        return this;
    }

    public Long getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Long userProfile) {
        this.creatorId = userProfile;
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

    // prettier-ignore
    @Override
    public String toString() {
        return "VideoStory{" +
            "id=" + getId() +
            ", thumbnail='" + getThumbnail() + "'" +
            ", thumbnailContentType='" + getThumbnailContentType() + "'" +
            ", thumbnailS3Key='" + getThumbnailS3Key() + "'" +
            ", content='" + getContent() + "'" +
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
