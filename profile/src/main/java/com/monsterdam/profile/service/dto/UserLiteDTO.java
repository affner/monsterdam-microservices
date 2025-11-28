package com.monsterdam.profile.service.dto;

import com.monsterdam.profile.domain.enumeration.ContentPreference;
import com.monsterdam.profile.domain.enumeration.UserGender;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.profile.domain.UserLite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserLiteDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] thumbnail;

    private String thumbnailContentType;
    private String thumbnailS3Key;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private UserGender gender;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    @NotNull
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$")
    private String nickName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$")
    private String fullName;

    @NotNull
    private ContentPreference contentPreference;

    private Long countryOfBirthId;

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

    public String getThumbnailS3Key() {
        return thumbnailS3Key;
    }

    public void setThumbnailS3Key(String thumbnailS3Key) {
        this.thumbnailS3Key = thumbnailS3Key;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public UserGender getGender() {
        return gender;
    }

    public void setGender(UserGender gender) {
        this.gender = gender;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ContentPreference getContentPreference() {
        return contentPreference;
    }

    public void setContentPreference(ContentPreference contentPreference) {
        this.contentPreference = contentPreference;
    }

    public Long getCountryOfBirthId() {
        return countryOfBirthId;
    }

    public void setCountryOfBirthId(Long countryOfBirthId) {
        this.countryOfBirthId = countryOfBirthId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserLiteDTO)) {
            return false;
        }

        UserLiteDTO userLiteDTO = (UserLiteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userLiteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserLiteDTO{" +
            "id=" + getId() +
            ", thumbnail='" + getThumbnail() + "'" +
            ", thumbnailS3Key='" + getThumbnailS3Key() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", nickName='" + getNickName() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", contentPreference='" + getContentPreference() + "'" +
            ", countryOfBirthId=" + getCountryOfBirthId() +
            "}";
    }
}
