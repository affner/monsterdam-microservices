package com.monsterdam.admin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.UserMention} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserMentionDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    private PostFeedDTO originPost;

    private PostCommentDTO originPostComment;

    private UserProfileDTO mentionedUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public PostFeedDTO getOriginPost() {
        return originPost;
    }

    public void setOriginPost(PostFeedDTO originPost) {
        this.originPost = originPost;
    }

    public PostCommentDTO getOriginPostComment() {
        return originPostComment;
    }

    public void setOriginPostComment(PostCommentDTO originPostComment) {
        this.originPostComment = originPostComment;
    }

    public UserProfileDTO getMentionedUser() {
        return mentionedUser;
    }

    public void setMentionedUser(UserProfileDTO mentionedUser) {
        this.mentionedUser = mentionedUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserMentionDTO)) {
            return false;
        }

        UserMentionDTO userMentionDTO = (UserMentionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userMentionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserMentionDTO{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", originPost=" + getOriginPost() +
            ", originPostComment=" + getOriginPostComment() +
            ", mentionedUser=" + getMentionedUser() +
            "}";
    }
}
