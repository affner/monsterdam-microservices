package com.fanflip.admin.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.DirectMessage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DirectMessageDTO implements Serializable {

    private Long id;

    @Lob
    private String messageContent;

    private Instant readDate;

    private Long likeCount;

    private Boolean isHidden;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    private ContentPackageDTO contentPackage;

    private DirectMessageDTO responseTo;

    private VideoStoryDTO repliedStory;

    private UserProfileDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Instant getReadDate() {
        return readDate;
    }

    public void setReadDate(Instant readDate) {
        this.readDate = readDate;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
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

    public ContentPackageDTO getContentPackage() {
        return contentPackage;
    }

    public void setContentPackage(ContentPackageDTO contentPackage) {
        this.contentPackage = contentPackage;
    }

    public DirectMessageDTO getResponseTo() {
        return responseTo;
    }

    public void setResponseTo(DirectMessageDTO responseTo) {
        this.responseTo = responseTo;
    }

    public VideoStoryDTO getRepliedStory() {
        return repliedStory;
    }

    public void setRepliedStory(VideoStoryDTO repliedStory) {
        this.repliedStory = repliedStory;
    }

    public UserProfileDTO getUser() {
        return user;
    }

    public void setUser(UserProfileDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DirectMessageDTO)) {
            return false;
        }

        DirectMessageDTO directMessageDTO = (DirectMessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, directMessageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DirectMessageDTO{" +
            "id=" + getId() +
            ", messageContent='" + getMessageContent() + "'" +
            ", readDate='" + getReadDate() + "'" +
            ", likeCount=" + getLikeCount() +
            ", isHidden='" + getIsHidden() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", contentPackage=" + getContentPackage() +
            ", responseTo=" + getResponseTo() +
            ", repliedStory=" + getRepliedStory() +
            ", user=" + getUser() +
            "}";
    }
}
