package com.monsterdam.admin.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.PersonalSocialLinks} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonalSocialLinksDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] thumbnail;

    private String thumbnailContentType;

    @Lob
    private byte[] normalImage;

    private String normalImageContentType;
    private String normalImageS3Key;

    private String thumbnailIconS3Key;

    @NotNull(message = "must not be null")
    private String socialLink;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    private SocialNetworkDTO socialNetwork;

    private UserProfileDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return thumbnailContentType;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public byte[] getNormalImage() {
        return normalImage;
    }

    public void setNormalImage(byte[] normalImage) {
        this.normalImage = normalImage;
    }

    public String getNormalImageContentType() {
        return normalImageContentType;
    }

    public void setNormalImageContentType(String normalImageContentType) {
        this.normalImageContentType = normalImageContentType;
    }

    public String getNormalImageS3Key() {
        return normalImageS3Key;
    }

    public void setNormalImageS3Key(String normalImageS3Key) {
        this.normalImageS3Key = normalImageS3Key;
    }

    public String getThumbnailIconS3Key() {
        return thumbnailIconS3Key;
    }

    public void setThumbnailIconS3Key(String thumbnailIconS3Key) {
        this.thumbnailIconS3Key = thumbnailIconS3Key;
    }

    public String getSocialLink() {
        return socialLink;
    }

    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;
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

    public SocialNetworkDTO getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(SocialNetworkDTO socialNetwork) {
        this.socialNetwork = socialNetwork;
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
        if (!(o instanceof PersonalSocialLinksDTO)) {
            return false;
        }

        PersonalSocialLinksDTO personalSocialLinksDTO = (PersonalSocialLinksDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personalSocialLinksDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalSocialLinksDTO{" +
            "id=" + getId() +
            ", thumbnail='" + getThumbnail() + "'" +
            ", normalImage='" + getNormalImage() + "'" +
            ", normalImageS3Key='" + getNormalImageS3Key() + "'" +
            ", thumbnailIconS3Key='" + getThumbnailIconS3Key() + "'" +
            ", socialLink='" + getSocialLink() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", socialNetwork=" + getSocialNetwork() +
            ", user=" + getUser() +
            "}";
    }
}
