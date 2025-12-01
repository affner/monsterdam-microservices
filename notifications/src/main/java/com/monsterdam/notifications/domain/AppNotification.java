package com.monsterdam.notifications.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A AppNotification.
 */
@Entity
@Table(name = "app_notification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "read_date")
    private Instant readDate;

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

    @Column(name = "post_comment_id")
    private Long postCommentId;

    @Column(name = "post_feed_id")
    private Long postFeedId;

    @Column(name = "direct_message_id")
    private Long directMessageId;

    @Column(name = "target_user_id")
    private Long targetUserId;

    @Column(name = "like_mark")
    private Long likeMark;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppNotification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReadDate() {
        return this.readDate;
    }

    public AppNotification readDate(Instant readDate) {
        this.setReadDate(readDate);
        return this;
    }

    public void setReadDate(Instant readDate) {
        this.readDate = readDate;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public AppNotification createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public AppNotification lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public AppNotification createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public AppNotification lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public AppNotification isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getPostCommentId() {
        return this.postCommentId;
    }

    public AppNotification postCommentId(Long postCommentId) {
        this.setPostCommentId(postCommentId);
        return this;
    }

    public void setPostCommentId(Long postCommentId) {
        this.postCommentId = postCommentId;
    }

    public Long getPostFeedId() {
        return this.postFeedId;
    }

    public AppNotification postFeedId(Long postFeedId) {
        this.setPostFeedId(postFeedId);
        return this;
    }

    public void setPostFeedId(Long postFeedId) {
        this.postFeedId = postFeedId;
    }

    public Long getDirectMessageId() {
        return this.directMessageId;
    }

    public AppNotification directMessageId(Long directMessageId) {
        this.setDirectMessageId(directMessageId);
        return this;
    }

    public void setDirectMessageId(Long directMessageId) {
        this.directMessageId = directMessageId;
    }

    public Long getTargetUserId() {
        return this.targetUserId;
    }

    public AppNotification targetUserId(Long targetUserId) {
        this.setTargetUserId(targetUserId);
        return this;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public Long getLikeMark() {
        return this.likeMark;
    }

    public AppNotification likeMark(Long likeMark) {
        this.setLikeMark(likeMark);
        return this;
    }

    public void setLikeMark(Long likeMark) {
        this.likeMark = likeMark;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppNotification)) {
            return false;
        }
        return getId() != null && getId().equals(((AppNotification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppNotification{" +
            "id=" + getId() +
            ", readDate='" + getReadDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", postCommentId=" + getPostCommentId() +
            ", postFeedId=" + getPostFeedId() +
            ", directMessageId=" + getDirectMessageId() +
            ", targetUserId=" + getTargetUserId() +
            ", likeMark=" + getLikeMark() +
            "}";
    }
}
