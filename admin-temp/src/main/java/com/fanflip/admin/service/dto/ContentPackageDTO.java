package com.fanflip.admin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.fanflip.admin.domain.ContentPackage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContentPackageDTO implements Serializable {

    private Long id;

    private BigDecimal amount;

    private Integer videoCount;

    private Integer imageCount;

    @NotNull(message = "must not be null")
    private Boolean isPaidContent;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    private SingleAudioDTO audio;

    private Set<UserProfileDTO> usersTaggeds = new HashSet<>();

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

    public SingleAudioDTO getAudio() {
        return audio;
    }

    public void setAudio(SingleAudioDTO audio) {
        this.audio = audio;
    }

    public Set<UserProfileDTO> getUsersTaggeds() {
        return usersTaggeds;
    }

    public void setUsersTaggeds(Set<UserProfileDTO> usersTaggeds) {
        this.usersTaggeds = usersTaggeds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentPackageDTO)) {
            return false;
        }

        ContentPackageDTO contentPackageDTO = (ContentPackageDTO) o;
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
            ", audio=" + getAudio() +
            ", usersTaggeds=" + getUsersTaggeds() +
            "}";
    }
}
