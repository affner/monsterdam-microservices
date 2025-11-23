package com.fanflip.bff.service.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for BFF data handling.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdminUserMentionDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    @NotNull
    private Long mentionedUserId;

    private AdminPostFeedDTO originPost;

    private AdminPostCommentDTO originPostComment;

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

    public Long getMentionedUserId() {
        return mentionedUserId;
    }

    public void setMentionedUserId(Long mentionedUserId) {
        this.mentionedUserId = mentionedUserId;
    }

    public AdminPostFeedDTO getOriginPost() {
        return originPost;
    }

    public void setOriginPost(AdminPostFeedDTO originPost) {
        this.originPost = originPost;
    }

    public AdminPostCommentDTO getOriginPostComment() {
        return originPostComment;
    }

    public void setOriginPostComment(AdminPostCommentDTO originPostComment) {
        this.originPostComment = originPostComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminUserMentionDTO)) {
            return false;
        }

        AdminUserMentionDTO userMentionDTO = (AdminUserMentionDTO) o;
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
            ", mentionedUserId=" + getMentionedUserId() +
            ", originPost=" + getOriginPost() +
            ", originPostComment=" + getOriginPostComment() +
            "}";
    }
}
