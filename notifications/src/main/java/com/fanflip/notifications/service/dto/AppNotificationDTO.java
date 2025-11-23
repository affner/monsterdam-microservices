package com.fanflip.notifications.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.notifications.domain.AppNotification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppNotificationDTO implements Serializable {

    private Long id;

    private Instant readDate;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    private Long postCommentId;

    private Long postFeedId;

    private Long directMessageId;

    private Long targetUserId;

    private Long likeMark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReadDate() {
        return readDate;
    }

    public void setReadDate(Instant readDate) {
        this.readDate = readDate;
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

    public Long getPostCommentId() {
        return postCommentId;
    }

    public void setPostCommentId(Long postCommentId) {
        this.postCommentId = postCommentId;
    }

    public Long getPostFeedId() {
        return postFeedId;
    }

    public void setPostFeedId(Long postFeedId) {
        this.postFeedId = postFeedId;
    }

    public Long getDirectMessageId() {
        return directMessageId;
    }

    public void setDirectMessageId(Long directMessageId) {
        this.directMessageId = directMessageId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public Long getLikeMark() {
        return likeMark;
    }

    public void setLikeMark(Long likeMark) {
        this.likeMark = likeMark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppNotificationDTO)) {
            return false;
        }

        AppNotificationDTO appNotificationDTO = (AppNotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appNotificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppNotificationDTO{" +
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
