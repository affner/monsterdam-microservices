package com.monsterdam.bff.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for BFF data handling.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPostCommentDTO implements Serializable {

    private Long id;

    @Lob
    private String commentContent;

    private Integer likeCount;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    @NotNull
    private Long commenterUserId;

    private UserPostFeedDTO post;

    private UserPostCommentDTO responseTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
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

    public Long getCommenterUserId() {
        return commenterUserId;
    }

    public void setCommenterUserId(Long commenterUserId) {
        this.commenterUserId = commenterUserId;
    }

    public UserPostFeedDTO getPost() {
        return post;
    }

    public void setPost(UserPostFeedDTO post) {
        this.post = post;
    }

    public UserPostCommentDTO getResponseTo() {
        return responseTo;
    }

    public void setResponseTo(UserPostCommentDTO responseTo) {
        this.responseTo = responseTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPostCommentDTO)) {
            return false;
        }

        UserPostCommentDTO postCommentDTO = (UserPostCommentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, postCommentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostCommentDTO{" +
            "id=" + getId() +
            ", commentContent='" + getCommentContent() + "'" +
            ", likeCount=" + getLikeCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", commenterUserId=" + getCommenterUserId() +
            ", post=" + getPost() +
            ", responseTo=" + getResponseTo() +
            "}";
    }
}
