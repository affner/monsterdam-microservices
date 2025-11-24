package com.monsterdam.interactions.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.interactions.domain.PostComment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostCommentDTO implements Serializable {

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

    private PostFeedDTO post;

    private PostCommentDTO responseTo;

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

    public PostFeedDTO getPost() {
        return post;
    }

    public void setPost(PostFeedDTO post) {
        this.post = post;
    }

    public PostCommentDTO getResponseTo() {
        return responseTo;
    }

    public void setResponseTo(PostCommentDTO responseTo) {
        this.responseTo = responseTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostCommentDTO)) {
            return false;
        }

        PostCommentDTO postCommentDTO = (PostCommentDTO) o;
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
