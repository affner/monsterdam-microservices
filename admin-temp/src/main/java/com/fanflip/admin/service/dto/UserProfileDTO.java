package com.fanflip.admin.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.fanflip.admin.domain.UserProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[a-z0-9_-]+$")
    private String emailContact;

    @Lob
    private byte[] profilePhoto;

    private String profilePhotoContentType;

    @Lob
    private byte[] coverPhoto;

    private String coverPhotoContentType;
    private String profilePhotoS3Key;

    private String coverPhotoS3Key;

    private String mainContentUrl;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    private String mobilePhone;

    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$")
    private String websiteUrl;

    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$")
    private String amazonWishlistUrl;

    @NotNull(message = "must not be null")
    private Instant lastLoginDate;

    @Lob
    private String biography;

    private Boolean isFree;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    private UserLiteDTO userLite;

    private UserSettingsDTO settings;

    private CountryDTO countryOfBirth;

    private StateDTO stateOfResidence;

    private Set<UserProfileDTO> followeds = new HashSet<>();

    private Set<UserProfileDTO> blockedLists = new HashSet<>();

    private Set<UserProfileDTO> loyaLists = new HashSet<>();

    private Set<UserProfileDTO> subscribeds = new HashSet<>();

    private Set<UserEventDTO> joinedEvents = new HashSet<>();

    private Set<StateDTO> blockedUbications = new HashSet<>();

    private Set<HashTagDTO> hashTags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfilePhotoContentType() {
        return profilePhotoContentType;
    }

    public void setProfilePhotoContentType(String profilePhotoContentType) {
        this.profilePhotoContentType = profilePhotoContentType;
    }

    public byte[] getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(byte[] coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getCoverPhotoContentType() {
        return coverPhotoContentType;
    }

    public void setCoverPhotoContentType(String coverPhotoContentType) {
        this.coverPhotoContentType = coverPhotoContentType;
    }

    public String getProfilePhotoS3Key() {
        return profilePhotoS3Key;
    }

    public void setProfilePhotoS3Key(String profilePhotoS3Key) {
        this.profilePhotoS3Key = profilePhotoS3Key;
    }

    public String getCoverPhotoS3Key() {
        return coverPhotoS3Key;
    }

    public void setCoverPhotoS3Key(String coverPhotoS3Key) {
        this.coverPhotoS3Key = coverPhotoS3Key;
    }

    public String getMainContentUrl() {
        return mainContentUrl;
    }

    public void setMainContentUrl(String mainContentUrl) {
        this.mainContentUrl = mainContentUrl;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getAmazonWishlistUrl() {
        return amazonWishlistUrl;
    }

    public void setAmazonWishlistUrl(String amazonWishlistUrl) {
        this.amazonWishlistUrl = amazonWishlistUrl;
    }

    public Instant getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Instant lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
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

    public UserLiteDTO getUserLite() {
        return userLite;
    }

    public void setUserLite(UserLiteDTO userLite) {
        this.userLite = userLite;
    }

    public UserSettingsDTO getSettings() {
        return settings;
    }

    public void setSettings(UserSettingsDTO settings) {
        this.settings = settings;
    }

    public CountryDTO getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(CountryDTO countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public StateDTO getStateOfResidence() {
        return stateOfResidence;
    }

    public void setStateOfResidence(StateDTO stateOfResidence) {
        this.stateOfResidence = stateOfResidence;
    }

    public Set<UserProfileDTO> getFolloweds() {
        return followeds;
    }

    public void setFolloweds(Set<UserProfileDTO> followeds) {
        this.followeds = followeds;
    }

    public Set<UserProfileDTO> getBlockedLists() {
        return blockedLists;
    }

    public void setBlockedLists(Set<UserProfileDTO> blockedLists) {
        this.blockedLists = blockedLists;
    }

    public Set<UserProfileDTO> getLoyaLists() {
        return loyaLists;
    }

    public void setLoyaLists(Set<UserProfileDTO> loyaLists) {
        this.loyaLists = loyaLists;
    }

    public Set<UserProfileDTO> getSubscribeds() {
        return subscribeds;
    }

    public void setSubscribeds(Set<UserProfileDTO> subscribeds) {
        this.subscribeds = subscribeds;
    }

    public Set<UserEventDTO> getJoinedEvents() {
        return joinedEvents;
    }

    public void setJoinedEvents(Set<UserEventDTO> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }

    public Set<StateDTO> getBlockedUbications() {
        return blockedUbications;
    }

    public void setBlockedUbications(Set<StateDTO> blockedUbications) {
        this.blockedUbications = blockedUbications;
    }

    public Set<HashTagDTO> getHashTags() {
        return hashTags;
    }

    public void setHashTags(Set<HashTagDTO> hashTags) {
        this.hashTags = hashTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfileDTO)) {
            return false;
        }

        UserProfileDTO userProfileDTO = (UserProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileDTO{" +
            "id=" + getId() +
            ", emailContact='" + getEmailContact() + "'" +
            ", profilePhoto='" + getProfilePhoto() + "'" +
            ", coverPhoto='" + getCoverPhoto() + "'" +
            ", profilePhotoS3Key='" + getProfilePhotoS3Key() + "'" +
            ", coverPhotoS3Key='" + getCoverPhotoS3Key() + "'" +
            ", mainContentUrl='" + getMainContentUrl() + "'" +
            ", mobilePhone='" + getMobilePhone() + "'" +
            ", websiteUrl='" + getWebsiteUrl() + "'" +
            ", amazonWishlistUrl='" + getAmazonWishlistUrl() + "'" +
            ", lastLoginDate='" + getLastLoginDate() + "'" +
            ", biography='" + getBiography() + "'" +
            ", isFree='" + getIsFree() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", userLite=" + getUserLite() +
            ", settings=" + getSettings() +
            ", countryOfBirth=" + getCountryOfBirth() +
            ", stateOfResidence=" + getStateOfResidence() +
            ", followeds=" + getFolloweds() +
            ", blockedLists=" + getBlockedLists() +
            ", loyaLists=" + getLoyaLists() +
            ", subscribeds=" + getSubscribeds() +
            ", joinedEvents=" + getJoinedEvents() +
            ", blockedUbications=" + getBlockedUbications() +
            ", hashTags=" + getHashTags() +
            "}";
    }
}
