package com.monsterdam.profile.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.profile.domain.PostFeedHashTagRelation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostFeedHashTagRelationDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    @NotNull
    private Long postFeedId;

    private HashTagDTO hashtag;

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

    public Long getPostFeedId() {
        return postFeedId;
    }

    public void setPostFeedId(Long postFeedId) {
        this.postFeedId = postFeedId;
    }

    public HashTagDTO getHashtag() {
        return hashtag;
    }

    public void setHashtag(HashTagDTO hashtag) {
        this.hashtag = hashtag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostFeedHashTagRelationDTO)) {
            return false;
        }

        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO = (PostFeedHashTagRelationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, postFeedHashTagRelationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostFeedHashTagRelationDTO{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", postFeedId=" + getPostFeedId() +
            ", hashtag=" + getHashtag() +
            "}";
    }
}
