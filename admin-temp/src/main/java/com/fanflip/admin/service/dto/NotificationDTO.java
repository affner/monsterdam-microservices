package com.fanflip.admin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.Notification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDTO implements Serializable {

    private Long id;

    private Instant readDate;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    private Long postCommentId;

    private Long postFeedId;

    private Long directMessageId;

    private Long userMentionId;

    private Long likeMarkId;

    private UserProfileDTO commentedUser;

    private UserProfileDTO messagedUser;

    private UserProfileDTO mentionerUserInPost;

    private UserProfileDTO mentionerUserInComment;

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

    public Long getUserMentionId() {
        return userMentionId;
    }

    public void setUserMentionId(Long userMentionId) {
        this.userMentionId = userMentionId;
    }

    public Long getLikeMarkId() {
        return likeMarkId;
    }

    public void setLikeMarkId(Long likeMarkId) {
        this.likeMarkId = likeMarkId;
    }

    public UserProfileDTO getCommentedUser() {
        return commentedUser;
    }

    public void setCommentedUser(UserProfileDTO commentedUser) {
        this.commentedUser = commentedUser;
    }

    public UserProfileDTO getMessagedUser() {
        return messagedUser;
    }

    public void setMessagedUser(UserProfileDTO messagedUser) {
        this.messagedUser = messagedUser;
    }

    public UserProfileDTO getMentionerUserInPost() {
        return mentionerUserInPost;
    }

    public void setMentionerUserInPost(UserProfileDTO mentionerUserInPost) {
        this.mentionerUserInPost = mentionerUserInPost;
    }

    public UserProfileDTO getMentionerUserInComment() {
        return mentionerUserInComment;
    }

    public void setMentionerUserInComment(UserProfileDTO mentionerUserInComment) {
        this.mentionerUserInComment = mentionerUserInComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDTO{" +
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
            ", userMentionId=" + getUserMentionId() +
            ", likeMarkId=" + getLikeMarkId() +
            ", commentedUser=" + getCommentedUser() +
            ", messagedUser=" + getMessagedUser() +
            ", mentionerUserInPost=" + getMentionerUserInPost() +
            ", mentionerUserInComment=" + getMentionerUserInComment() +
            "}";
    }
}
