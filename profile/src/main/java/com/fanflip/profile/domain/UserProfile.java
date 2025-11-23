package com.fanflip.profile.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "email_contact", nullable = false)
    private String emailContact;

    @Lob
    @Column(name = "profile_photo")
    private byte[] profilePhoto;

    @Column(name = "profile_photo_content_type")
    private String profilePhotoContentType;

    @Lob
    @Column(name = "cover_photo")
    private byte[] coverPhoto;

    @Column(name = "cover_photo_content_type")
    private String coverPhotoContentType;

    @Column(name = "profile_photo_s_3_key")
    private String profilePhotoS3Key;

    @Column(name = "cover_photo_s_3_key")
    private String coverPhotoS3Key;

    @Column(name = "main_content_url")
    private String mainContentUrl;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "amazon_wishlist_url")
    private String amazonWishlistUrl;

    @NotNull
    @Column(name = "last_login_date", nullable = false)
    private Instant lastLoginDate;

    @Lob
    @Column(name = "biography")
    private String biography;

    @Column(name = "is_free")
    private Boolean isFree;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "state_of_residence_id")
    private Long stateOfResidenceId;

    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private UserSettings settings;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Set<PersonalSocialLinks> socialNetworks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Set<StateUserRelation> blockedUbications = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_profile__followed",
        joinColumns = @JoinColumn(name = "user_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "settings",
            "socialNetworks",
            "blockedUbications",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "hashtags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
        },
        allowSetters = true
    )
    private Set<UserProfile> followeds = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_profile__blocked_list",
        joinColumns = @JoinColumn(name = "user_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "blocked_list_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "settings",
            "socialNetworks",
            "blockedUbications",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "hashtags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
        },
        allowSetters = true
    )
    private Set<UserProfile> blockedLists = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_profile__loya_lists",
        joinColumns = @JoinColumn(name = "user_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "loya_lists_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "settings",
            "socialNetworks",
            "blockedUbications",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "hashtags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
        },
        allowSetters = true
    )
    private Set<UserProfile> loyaLists = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_profile__subscribed",
        joinColumns = @JoinColumn(name = "user_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "subscribed_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "settings",
            "socialNetworks",
            "blockedUbications",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "hashtags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
        },
        allowSetters = true
    )
    private Set<UserProfile> subscribeds = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_profile__joined_events",
        joinColumns = @JoinColumn(name = "user_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "joined_events_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "members" }, allowSetters = true)
    private Set<UserEvent> joinedEvents = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_profile__hashtags",
        joinColumns = @JoinColumn(name = "user_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "hashtags_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "postFeeds", "users" }, allowSetters = true)
    private Set<HashTag> hashtags = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "followeds")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "settings",
            "socialNetworks",
            "blockedUbications",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "hashtags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
        },
        allowSetters = true
    )
    private Set<UserProfile> followers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "blockedLists")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "settings",
            "socialNetworks",
            "blockedUbications",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "hashtags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
        },
        allowSetters = true
    )
    private Set<UserProfile> blockers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "loyaLists")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "settings",
            "socialNetworks",
            "blockedUbications",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "hashtags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
        },
        allowSetters = true
    )
    private Set<UserProfile> awards = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subscribeds")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "settings",
            "socialNetworks",
            "blockedUbications",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "hashtags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
        },
        allowSetters = true
    )
    private Set<UserProfile> subscriptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailContact() {
        return this.emailContact;
    }

    public UserProfile emailContact(String emailContact) {
        this.setEmailContact(emailContact);
        return this;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public byte[] getProfilePhoto() {
        return this.profilePhoto;
    }

    public UserProfile profilePhoto(byte[] profilePhoto) {
        this.setProfilePhoto(profilePhoto);
        return this;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfilePhotoContentType() {
        return this.profilePhotoContentType;
    }

    public UserProfile profilePhotoContentType(String profilePhotoContentType) {
        this.profilePhotoContentType = profilePhotoContentType;
        return this;
    }

    public void setProfilePhotoContentType(String profilePhotoContentType) {
        this.profilePhotoContentType = profilePhotoContentType;
    }

    public byte[] getCoverPhoto() {
        return this.coverPhoto;
    }

    public UserProfile coverPhoto(byte[] coverPhoto) {
        this.setCoverPhoto(coverPhoto);
        return this;
    }

    public void setCoverPhoto(byte[] coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getCoverPhotoContentType() {
        return this.coverPhotoContentType;
    }

    public UserProfile coverPhotoContentType(String coverPhotoContentType) {
        this.coverPhotoContentType = coverPhotoContentType;
        return this;
    }

    public void setCoverPhotoContentType(String coverPhotoContentType) {
        this.coverPhotoContentType = coverPhotoContentType;
    }

    public String getProfilePhotoS3Key() {
        return this.profilePhotoS3Key;
    }

    public UserProfile profilePhotoS3Key(String profilePhotoS3Key) {
        this.setProfilePhotoS3Key(profilePhotoS3Key);
        return this;
    }

    public void setProfilePhotoS3Key(String profilePhotoS3Key) {
        this.profilePhotoS3Key = profilePhotoS3Key;
    }

    public String getCoverPhotoS3Key() {
        return this.coverPhotoS3Key;
    }

    public UserProfile coverPhotoS3Key(String coverPhotoS3Key) {
        this.setCoverPhotoS3Key(coverPhotoS3Key);
        return this;
    }

    public void setCoverPhotoS3Key(String coverPhotoS3Key) {
        this.coverPhotoS3Key = coverPhotoS3Key;
    }

    public String getMainContentUrl() {
        return this.mainContentUrl;
    }

    public UserProfile mainContentUrl(String mainContentUrl) {
        this.setMainContentUrl(mainContentUrl);
        return this;
    }

    public void setMainContentUrl(String mainContentUrl) {
        this.mainContentUrl = mainContentUrl;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public UserProfile mobilePhone(String mobilePhone) {
        this.setMobilePhone(mobilePhone);
        return this;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getWebsiteUrl() {
        return this.websiteUrl;
    }

    public UserProfile websiteUrl(String websiteUrl) {
        this.setWebsiteUrl(websiteUrl);
        return this;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getAmazonWishlistUrl() {
        return this.amazonWishlistUrl;
    }

    public UserProfile amazonWishlistUrl(String amazonWishlistUrl) {
        this.setAmazonWishlistUrl(amazonWishlistUrl);
        return this;
    }

    public void setAmazonWishlistUrl(String amazonWishlistUrl) {
        this.amazonWishlistUrl = amazonWishlistUrl;
    }

    public Instant getLastLoginDate() {
        return this.lastLoginDate;
    }

    public UserProfile lastLoginDate(Instant lastLoginDate) {
        this.setLastLoginDate(lastLoginDate);
        return this;
    }

    public void setLastLoginDate(Instant lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getBiography() {
        return this.biography;
    }

    public UserProfile biography(String biography) {
        this.setBiography(biography);
        return this;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Boolean getIsFree() {
        return this.isFree;
    }

    public UserProfile isFree(Boolean isFree) {
        this.setIsFree(isFree);
        return this;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserProfile createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserProfile lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserProfile createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserProfile lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public UserProfile isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getStateOfResidenceId() {
        return this.stateOfResidenceId;
    }

    public UserProfile stateOfResidenceId(Long stateOfResidenceId) {
        this.setStateOfResidenceId(stateOfResidenceId);
        return this;
    }

    public void setStateOfResidenceId(Long stateOfResidenceId) {
        this.stateOfResidenceId = stateOfResidenceId;
    }

    public UserSettings getSettings() {
        return this.settings;
    }

    public void setSettings(UserSettings userSettings) {
        this.settings = userSettings;
    }

    public UserProfile settings(UserSettings userSettings) {
        this.setSettings(userSettings);
        return this;
    }

    public Set<PersonalSocialLinks> getSocialNetworks() {
        return this.socialNetworks;
    }

    public void setSocialNetworks(Set<PersonalSocialLinks> personalSocialLinks) {
        if (this.socialNetworks != null) {
            this.socialNetworks.forEach(i -> i.setUser(null));
        }
        if (personalSocialLinks != null) {
            personalSocialLinks.forEach(i -> i.setUser(this));
        }
        this.socialNetworks = personalSocialLinks;
    }

    public UserProfile socialNetworks(Set<PersonalSocialLinks> personalSocialLinks) {
        this.setSocialNetworks(personalSocialLinks);
        return this;
    }

    public UserProfile addSocialNetworks(PersonalSocialLinks personalSocialLinks) {
        this.socialNetworks.add(personalSocialLinks);
        personalSocialLinks.setUser(this);
        return this;
    }

    public UserProfile removeSocialNetworks(PersonalSocialLinks personalSocialLinks) {
        this.socialNetworks.remove(personalSocialLinks);
        personalSocialLinks.setUser(null);
        return this;
    }

    public Set<StateUserRelation> getBlockedUbications() {
        return this.blockedUbications;
    }

    public void setBlockedUbications(Set<StateUserRelation> stateUserRelations) {
        if (this.blockedUbications != null) {
            this.blockedUbications.forEach(i -> i.setUser(null));
        }
        if (stateUserRelations != null) {
            stateUserRelations.forEach(i -> i.setUser(this));
        }
        this.blockedUbications = stateUserRelations;
    }

    public UserProfile blockedUbications(Set<StateUserRelation> stateUserRelations) {
        this.setBlockedUbications(stateUserRelations);
        return this;
    }

    public UserProfile addBlockedUbications(StateUserRelation stateUserRelation) {
        this.blockedUbications.add(stateUserRelation);
        stateUserRelation.setUser(this);
        return this;
    }

    public UserProfile removeBlockedUbications(StateUserRelation stateUserRelation) {
        this.blockedUbications.remove(stateUserRelation);
        stateUserRelation.setUser(null);
        return this;
    }

    public Set<UserProfile> getFolloweds() {
        return this.followeds;
    }

    public void setFolloweds(Set<UserProfile> userProfiles) {
        this.followeds = userProfiles;
    }

    public UserProfile followeds(Set<UserProfile> userProfiles) {
        this.setFolloweds(userProfiles);
        return this;
    }

    public UserProfile addFollowed(UserProfile userProfile) {
        this.followeds.add(userProfile);
        return this;
    }

    public UserProfile removeFollowed(UserProfile userProfile) {
        this.followeds.remove(userProfile);
        return this;
    }

    public Set<UserProfile> getBlockedLists() {
        return this.blockedLists;
    }

    public void setBlockedLists(Set<UserProfile> userProfiles) {
        this.blockedLists = userProfiles;
    }

    public UserProfile blockedLists(Set<UserProfile> userProfiles) {
        this.setBlockedLists(userProfiles);
        return this;
    }

    public UserProfile addBlockedList(UserProfile userProfile) {
        this.blockedLists.add(userProfile);
        return this;
    }

    public UserProfile removeBlockedList(UserProfile userProfile) {
        this.blockedLists.remove(userProfile);
        return this;
    }

    public Set<UserProfile> getLoyaLists() {
        return this.loyaLists;
    }

    public void setLoyaLists(Set<UserProfile> userProfiles) {
        this.loyaLists = userProfiles;
    }

    public UserProfile loyaLists(Set<UserProfile> userProfiles) {
        this.setLoyaLists(userProfiles);
        return this;
    }

    public UserProfile addLoyaLists(UserProfile userProfile) {
        this.loyaLists.add(userProfile);
        return this;
    }

    public UserProfile removeLoyaLists(UserProfile userProfile) {
        this.loyaLists.remove(userProfile);
        return this;
    }

    public Set<UserProfile> getSubscribeds() {
        return this.subscribeds;
    }

    public void setSubscribeds(Set<UserProfile> userProfiles) {
        this.subscribeds = userProfiles;
    }

    public UserProfile subscribeds(Set<UserProfile> userProfiles) {
        this.setSubscribeds(userProfiles);
        return this;
    }

    public UserProfile addSubscribed(UserProfile userProfile) {
        this.subscribeds.add(userProfile);
        return this;
    }

    public UserProfile removeSubscribed(UserProfile userProfile) {
        this.subscribeds.remove(userProfile);
        return this;
    }

    public Set<UserEvent> getJoinedEvents() {
        return this.joinedEvents;
    }

    public void setJoinedEvents(Set<UserEvent> userEvents) {
        this.joinedEvents = userEvents;
    }

    public UserProfile joinedEvents(Set<UserEvent> userEvents) {
        this.setJoinedEvents(userEvents);
        return this;
    }

    public UserProfile addJoinedEvents(UserEvent userEvent) {
        this.joinedEvents.add(userEvent);
        return this;
    }

    public UserProfile removeJoinedEvents(UserEvent userEvent) {
        this.joinedEvents.remove(userEvent);
        return this;
    }

    public Set<HashTag> getHashtags() {
        return this.hashtags;
    }

    public void setHashtags(Set<HashTag> hashTags) {
        this.hashtags = hashTags;
    }

    public UserProfile hashtags(Set<HashTag> hashTags) {
        this.setHashtags(hashTags);
        return this;
    }

    public UserProfile addHashtags(HashTag hashTag) {
        this.hashtags.add(hashTag);
        return this;
    }

    public UserProfile removeHashtags(HashTag hashTag) {
        this.hashtags.remove(hashTag);
        return this;
    }

    public Set<UserProfile> getFollowers() {
        return this.followers;
    }

    public void setFollowers(Set<UserProfile> userProfiles) {
        if (this.followers != null) {
            this.followers.forEach(i -> i.removeFollowed(this));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.addFollowed(this));
        }
        this.followers = userProfiles;
    }

    public UserProfile followers(Set<UserProfile> userProfiles) {
        this.setFollowers(userProfiles);
        return this;
    }

    public UserProfile addFollowers(UserProfile userProfile) {
        this.followers.add(userProfile);
        userProfile.getFolloweds().add(this);
        return this;
    }

    public UserProfile removeFollowers(UserProfile userProfile) {
        this.followers.remove(userProfile);
        userProfile.getFolloweds().remove(this);
        return this;
    }

    public Set<UserProfile> getBlockers() {
        return this.blockers;
    }

    public void setBlockers(Set<UserProfile> userProfiles) {
        if (this.blockers != null) {
            this.blockers.forEach(i -> i.removeBlockedList(this));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.addBlockedList(this));
        }
        this.blockers = userProfiles;
    }

    public UserProfile blockers(Set<UserProfile> userProfiles) {
        this.setBlockers(userProfiles);
        return this;
    }

    public UserProfile addBlockers(UserProfile userProfile) {
        this.blockers.add(userProfile);
        userProfile.getBlockedLists().add(this);
        return this;
    }

    public UserProfile removeBlockers(UserProfile userProfile) {
        this.blockers.remove(userProfile);
        userProfile.getBlockedLists().remove(this);
        return this;
    }

    public Set<UserProfile> getAwards() {
        return this.awards;
    }

    public void setAwards(Set<UserProfile> userProfiles) {
        if (this.awards != null) {
            this.awards.forEach(i -> i.removeLoyaLists(this));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.addLoyaLists(this));
        }
        this.awards = userProfiles;
    }

    public UserProfile awards(Set<UserProfile> userProfiles) {
        this.setAwards(userProfiles);
        return this;
    }

    public UserProfile addAwards(UserProfile userProfile) {
        this.awards.add(userProfile);
        userProfile.getLoyaLists().add(this);
        return this;
    }

    public UserProfile removeAwards(UserProfile userProfile) {
        this.awards.remove(userProfile);
        userProfile.getLoyaLists().remove(this);
        return this;
    }

    public Set<UserProfile> getSubscriptions() {
        return this.subscriptions;
    }

    public void setSubscriptions(Set<UserProfile> userProfiles) {
        if (this.subscriptions != null) {
            this.subscriptions.forEach(i -> i.removeSubscribed(this));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.addSubscribed(this));
        }
        this.subscriptions = userProfiles;
    }

    public UserProfile subscriptions(Set<UserProfile> userProfiles) {
        this.setSubscriptions(userProfiles);
        return this;
    }

    public UserProfile addSubscriptions(UserProfile userProfile) {
        this.subscriptions.add(userProfile);
        userProfile.getSubscribeds().add(this);
        return this;
    }

    public UserProfile removeSubscriptions(UserProfile userProfile) {
        this.subscriptions.remove(userProfile);
        userProfile.getSubscribeds().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", emailContact='" + getEmailContact() + "'" +
            ", profilePhoto='" + getProfilePhoto() + "'" +
            ", profilePhotoContentType='" + getProfilePhotoContentType() + "'" +
            ", coverPhoto='" + getCoverPhoto() + "'" +
            ", coverPhotoContentType='" + getCoverPhotoContentType() + "'" +
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
            ", stateOfResidenceId=" + getStateOfResidenceId() +
            "}";
    }
}
