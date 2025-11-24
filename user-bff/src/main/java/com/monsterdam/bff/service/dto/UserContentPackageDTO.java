package com.monsterdam.bff.service.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;


/**
 * DTO for BFF data handling.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserContentPackageDTO implements Serializable {

    private Long id;

    private BigDecimal amount;

    private Integer videoCount;

    private Integer imageCount;

    @NotNull
    private Boolean isPaidContent;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    private Long messageId;

    private Long postId;

    private UserSingleAudioDTO audio;

    private List<UserSingleAudioDTO> videos;

    private List<UserSingleAudioDTO> photos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }

    public Integer getImageCount() {
        return imageCount;
    }

    public void setImageCount(Integer imageCount) {
        this.imageCount = imageCount;
    }

    public Boolean getIsPaidContent() {
        return isPaidContent;
    }

    public void setIsPaidContent(Boolean isPaidContent) {
        this.isPaidContent = isPaidContent;
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

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public UserSingleAudioDTO getAudio() {
        return audio;
    }

    public void setAudio(UserSingleAudioDTO audio) {
        this.audio = audio;
    }

    public List<UserSingleAudioDTO> getVideos() {
        return videos;
    }

    public void setVideos(List<UserSingleAudioDTO> videos) {
        this.videos = videos;
    }

    public List<UserSingleAudioDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(List<UserSingleAudioDTO> photos) {
        this.photos = photos;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserContentPackageDTO)) {
            return false;
        }

        UserContentPackageDTO contentPackageDTO = (UserContentPackageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contentPackageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContentPackageDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", videoCount=" + getVideoCount() +
            ", imageCount=" + getImageCount() +
            ", isPaidContent='" + getIsPaidContent() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", messageId=" + getMessageId() +
            ", postId=" + getPostId() +
            ", audio=" + getAudio() +
            "}";
    }


}
