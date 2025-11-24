package com.monsterdam.admin.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.PostFeed} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostFeedDTO implements Serializable {

    private Long id;

    @Lob
    private String postContent;

    private Boolean isHidden;

    private Boolean pinnedPost;

    private Long likeCount;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    private PostPollDTO poll;

    private ContentPackageDTO contentPackage;

    private Set<HashTagDTO> hashTags = new HashSet<>();

    private UserProfileDTO creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public Boolean getPinnedPost() {
        return pinnedPost;
    }

    public void setPinnedPost(Boolean pinnedPost) {
        this.pinnedPost = pinnedPost;
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

    public PostPollDTO getPoll() {
        return poll;
    }

    public void setPoll(PostPollDTO poll) {
        this.poll = poll;
    }

    public ContentPackageDTO getContentPackage() {
        return contentPackage;
    }

    public void setContentPackage(ContentPackageDTO contentPackage) {
        this.contentPackage = contentPackage;
    }

    public Set<HashTagDTO> getHashTags() {
        return hashTags;
    }

    public void setHashTags(Set<HashTagDTO> hashTags) {
        this.hashTags = hashTags;
    }

    public UserProfileDTO getCreator() {
        return creator;
    }

    public void setCreator(UserProfileDTO creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostFeedDTO)) {
            return false;
        }

        PostFeedDTO postFeedDTO = (PostFeedDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, postFeedDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostFeedDTO{" +
            "id=" + getId() +
            ", postContent='" + getPostContent() + "'" +
            ", isHidden='" + getIsHidden() + "'" +
            ", pinnedPost='" + getPinnedPost() + "'" +
            ", likeCount=" + getLikeCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", poll=" + getPoll() +
            ", contentPackage=" + getContentPackage() +
            ", hashTags=" + getHashTags() +
            ", creator=" + getCreator() +
            "}";
    }
}
