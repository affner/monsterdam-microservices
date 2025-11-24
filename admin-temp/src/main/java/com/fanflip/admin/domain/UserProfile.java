package com.monsterdam.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UserProfile.
 */
@Table("user_profile")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "userprofile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[a-z0-9_-]+$")
    @Column("email_contact")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String emailContact;

    @Column("profile_photo")
    private byte[] profilePhoto;

    @Column("profile_photo_content_type")
    private String profilePhotoContentType;

    @Column("cover_photo")
    private byte[] coverPhoto;

    @Column("cover_photo_content_type")
    private String coverPhotoContentType;

    @Column("profile_photo_s_3_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String profilePhotoS3Key;

    @Column("cover_photo_s_3_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String coverPhotoS3Key;

    @Column("main_content_url")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String mainContentUrl;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    @Column("mobile_phone")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String mobilePhone;

    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$")
    @Column("website_url")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String websiteUrl;

    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$")
    @Column("amazon_wishlist_url")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String amazonWishlistUrl;

    @NotNull(message = "must not be null")
    @Column("last_login_date")
    private Instant lastLoginDate;

    @Column("biography")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String biography;

    @Column("is_free")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isFree;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private Instant createdDate;

    @Column("last_modified_date")
    private Instant lastModifiedDate;

    @Column("created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    @Column("last_modified_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isDeleted;

    @Transient
    private UserLite userLite;

    @Transient
    private UserSettings settings;

    @Transient
    @JsonIgnoreProperties(
        value = { "ticket", "reporter", "reported", "story", "video", "photo", "audio", "liveStream", "message", "post", "postComment" },
        allowSetters = true
    )
    private Set<UserReport> makedReports = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = { "ticket", "reporter", "reported", "story", "video", "photo", "audio", "liveStream", "message", "post", "postComment" },
        allowSetters = true
    )
    private Set<UserReport> receivedReports = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "moderationAction", "report", "documentsReview", "assignedAdmin", "user" }, allowSetters = true)
    private Set<AssistanceTicket> assistanceTickets = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "creatorEarning", "creator" }, allowSetters = true)
    private Set<MoneyPayout> withdraws = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "selledSubscriptions", "creator" }, allowSetters = true)
    private Set<SubscriptionBundle> subscriptionBundles = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = { "creator", "moneyPayout", "purchasedContent", "purchasedSubscription", "purchasedTip" },
        allowSetters = true
    )
    private Set<CreatorEarning> earnings = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = {
            "paymentMethod",
            "paymentProvider",
            "viewer",
            "accountingRecord",
            "purchasedContent",
            "purchasedSubscription",
            "walletTransaction",
            "purchasedTip",
        },
        allowSetters = true
    )
    private Set<PaymentTransaction> payments = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "purchasedSubscriptions", "creator" }, allowSetters = true)
    private Set<OfferPromotion> planOffers = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = { "payment", "walletTransaction", "creatorEarning", "viewer", "purchasedContentPackage" },
        allowSetters = true
    )
    private Set<PurchasedContent> purchasedContents = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = { "payment", "walletTransaction", "creatorEarning", "subscriptionBundle", "appliedPromotion", "viewer" },
        allowSetters = true
    )
    private Set<PurchasedSubscription> purchasedSubscriptions = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "payment", "viewer", "purchasedContent", "purchasedSubscription", "purchasedTip" }, allowSetters = true)
    private Set<WalletTransaction> wallets = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "socialNetwork", "user" }, allowSetters = true)
    private Set<PersonalSocialLinks> socialNetworks = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "commentedUser", "messagedUser", "mentionerUserInPost", "mentionerUserInComment" }, allowSetters = true)
    private Set<Notification> commentNotifications = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "commentedUser", "messagedUser", "mentionerUserInPost", "mentionerUserInComment" }, allowSetters = true)
    private Set<Notification> messageNotifications = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "commentedUser", "messagedUser", "mentionerUserInPost", "mentionerUserInComment" }, allowSetters = true)
    private Set<Notification> userMentionNotifications = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "commentedUser", "messagedUser", "mentionerUserInPost", "mentionerUserInComment" }, allowSetters = true)
    private Set<Notification> commentMentionNotifications = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "owner" }, allowSetters = true)
    private Set<UserAssociation> ownAccountsAssociations = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "creator", "members" }, allowSetters = true)
    private Set<UserEvent> createdEvents = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "post", "message", "user" }, allowSetters = true)
    private Set<BookMark> bookMarks = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "creator" }, allowSetters = true)
    private Set<Feedback> feedbacks = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = {
            "contentPackage",
            "reports",
            "responses",
            "responseTo",
            "repliedStory",
            "user",
            "bookMarks",
            "chatRooms",
            "adminAnnouncement",
            "purchasedTip",
        },
        allowSetters = true
    )
    private Set<DirectMessage> sentMessages = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "sentMessages", "user" }, allowSetters = true)
    private Set<ChatRoom> chats = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "originPost", "originPostComment", "mentionedUser" }, allowSetters = true)
    private Set<UserMention> mentions = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "reports", "responses", "commentMentions", "post", "responseTo", "commenter" }, allowSetters = true)
    private Set<PostComment> comments = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = { "poll", "contentPackage", "reports", "comments", "commentMentions", "hashTags", "creator", "bookMarks" },
        allowSetters = true
    )
    private Set<PostFeed> feeds = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "pollOption", "votingUser" }, allowSetters = true)
    private Set<PollVote> votedPolls = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "reports", "messages", "creator" }, allowSetters = true)
    private Set<VideoStory> videoStories = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Set<SingleDocument> documents = new HashSet<>();

    @Transient
    private Country countryOfBirth;

    @Transient
    @JsonIgnoreProperties(value = { "country", "blockers" }, allowSetters = true)
    private State stateOfResidence;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "userLite",
            "settings",
            "makedReports",
            "receivedReports",
            "assistanceTickets",
            "withdraws",
            "subscriptionBundles",
            "earnings",
            "payments",
            "planOffers",
            "purchasedContents",
            "purchasedSubscriptions",
            "wallets",
            "socialNetworks",
            "commentNotifications",
            "messageNotifications",
            "userMentionNotifications",
            "commentMentionNotifications",
            "ownAccountsAssociations",
            "createdEvents",
            "bookMarks",
            "feedbacks",
            "sentMessages",
            "chats",
            "mentions",
            "comments",
            "feeds",
            "votedPolls",
            "videoStories",
            "documents",
            "countryOfBirth",
            "stateOfResidence",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "blockedUbications",
            "hashTags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
            "contentPackageTags",
        },
        allowSetters = true
    )
    private Set<UserProfile> followeds = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = {
            "userLite",
            "settings",
            "makedReports",
            "receivedReports",
            "assistanceTickets",
            "withdraws",
            "subscriptionBundles",
            "earnings",
            "payments",
            "planOffers",
            "purchasedContents",
            "purchasedSubscriptions",
            "wallets",
            "socialNetworks",
            "commentNotifications",
            "messageNotifications",
            "userMentionNotifications",
            "commentMentionNotifications",
            "ownAccountsAssociations",
            "createdEvents",
            "bookMarks",
            "feedbacks",
            "sentMessages",
            "chats",
            "mentions",
            "comments",
            "feeds",
            "votedPolls",
            "videoStories",
            "documents",
            "countryOfBirth",
            "stateOfResidence",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "blockedUbications",
            "hashTags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
            "contentPackageTags",
        },
        allowSetters = true
    )
    private Set<UserProfile> blockedLists = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = {
            "userLite",
            "settings",
            "makedReports",
            "receivedReports",
            "assistanceTickets",
            "withdraws",
            "subscriptionBundles",
            "earnings",
            "payments",
            "planOffers",
            "purchasedContents",
            "purchasedSubscriptions",
            "wallets",
            "socialNetworks",
            "commentNotifications",
            "messageNotifications",
            "userMentionNotifications",
            "commentMentionNotifications",
            "ownAccountsAssociations",
            "createdEvents",
            "bookMarks",
            "feedbacks",
            "sentMessages",
            "chats",
            "mentions",
            "comments",
            "feeds",
            "votedPolls",
            "videoStories",
            "documents",
            "countryOfBirth",
            "stateOfResidence",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "blockedUbications",
            "hashTags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
            "contentPackageTags",
        },
        allowSetters = true
    )
    private Set<UserProfile> loyaLists = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = {
            "userLite",
            "settings",
            "makedReports",
            "receivedReports",
            "assistanceTickets",
            "withdraws",
            "subscriptionBundles",
            "earnings",
            "payments",
            "planOffers",
            "purchasedContents",
            "purchasedSubscriptions",
            "wallets",
            "socialNetworks",
            "commentNotifications",
            "messageNotifications",
            "userMentionNotifications",
            "commentMentionNotifications",
            "ownAccountsAssociations",
            "createdEvents",
            "bookMarks",
            "feedbacks",
            "sentMessages",
            "chats",
            "mentions",
            "comments",
            "feeds",
            "votedPolls",
            "videoStories",
            "documents",
            "countryOfBirth",
            "stateOfResidence",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "blockedUbications",
            "hashTags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
            "contentPackageTags",
        },
        allowSetters = true
    )
    private Set<UserProfile> subscribeds = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "creator", "members" }, allowSetters = true)
    private Set<UserEvent> joinedEvents = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "country", "blockers" }, allowSetters = true)
    private Set<State> blockedUbications = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "posts", "profiles" }, allowSetters = true)
    private Set<HashTag> hashTags = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = {
            "userLite",
            "settings",
            "makedReports",
            "receivedReports",
            "assistanceTickets",
            "withdraws",
            "subscriptionBundles",
            "earnings",
            "payments",
            "planOffers",
            "purchasedContents",
            "purchasedSubscriptions",
            "wallets",
            "socialNetworks",
            "commentNotifications",
            "messageNotifications",
            "userMentionNotifications",
            "commentMentionNotifications",
            "ownAccountsAssociations",
            "createdEvents",
            "bookMarks",
            "feedbacks",
            "sentMessages",
            "chats",
            "mentions",
            "comments",
            "feeds",
            "votedPolls",
            "videoStories",
            "documents",
            "countryOfBirth",
            "stateOfResidence",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "blockedUbications",
            "hashTags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
            "contentPackageTags",
        },
        allowSetters = true
    )
    private Set<UserProfile> followers = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = {
            "userLite",
            "settings",
            "makedReports",
            "receivedReports",
            "assistanceTickets",
            "withdraws",
            "subscriptionBundles",
            "earnings",
            "payments",
            "planOffers",
            "purchasedContents",
            "purchasedSubscriptions",
            "wallets",
            "socialNetworks",
            "commentNotifications",
            "messageNotifications",
            "userMentionNotifications",
            "commentMentionNotifications",
            "ownAccountsAssociations",
            "createdEvents",
            "bookMarks",
            "feedbacks",
            "sentMessages",
            "chats",
            "mentions",
            "comments",
            "feeds",
            "votedPolls",
            "videoStories",
            "documents",
            "countryOfBirth",
            "stateOfResidence",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "blockedUbications",
            "hashTags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
            "contentPackageTags",
        },
        allowSetters = true
    )
    private Set<UserProfile> blockers = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = {
            "userLite",
            "settings",
            "makedReports",
            "receivedReports",
            "assistanceTickets",
            "withdraws",
            "subscriptionBundles",
            "earnings",
            "payments",
            "planOffers",
            "purchasedContents",
            "purchasedSubscriptions",
            "wallets",
            "socialNetworks",
            "commentNotifications",
            "messageNotifications",
            "userMentionNotifications",
            "commentMentionNotifications",
            "ownAccountsAssociations",
            "createdEvents",
            "bookMarks",
            "feedbacks",
            "sentMessages",
            "chats",
            "mentions",
            "comments",
            "feeds",
            "votedPolls",
            "videoStories",
            "documents",
            "countryOfBirth",
            "stateOfResidence",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "blockedUbications",
            "hashTags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
            "contentPackageTags",
        },
        allowSetters = true
    )
    private Set<UserProfile> awards = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = {
            "userLite",
            "settings",
            "makedReports",
            "receivedReports",
            "assistanceTickets",
            "withdraws",
            "subscriptionBundles",
            "earnings",
            "payments",
            "planOffers",
            "purchasedContents",
            "purchasedSubscriptions",
            "wallets",
            "socialNetworks",
            "commentNotifications",
            "messageNotifications",
            "userMentionNotifications",
            "commentMentionNotifications",
            "ownAccountsAssociations",
            "createdEvents",
            "bookMarks",
            "feedbacks",
            "sentMessages",
            "chats",
            "mentions",
            "comments",
            "feeds",
            "votedPolls",
            "videoStories",
            "documents",
            "countryOfBirth",
            "stateOfResidence",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "blockedUbications",
            "hashTags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
            "contentPackageTags",
        },
        allowSetters = true
    )
    private Set<UserProfile> subscriptions = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "audio", "selledPackages", "videos", "photos", "usersTaggeds", "message", "post" }, allowSetters = true)
    private Set<ContentPackage> contentPackageTags = new HashSet<>();

    @Column("user_lite_id")
    private Long userLiteId;

    @Column("settings_id")
    private Long settingsId;

    @Column("country_of_birth_id")
    private Long countryOfBirthId;

    @Column("state_of_residence_id")
    private Long stateOfResidenceId;

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

    public UserLite getUserLite() {
        return this.userLite;
    }

    public void setUserLite(UserLite userLite) {
        this.userLite = userLite;
        this.userLiteId = userLite != null ? userLite.getId() : null;
    }

    public UserProfile userLite(UserLite userLite) {
        this.setUserLite(userLite);
        return this;
    }

    public UserSettings getSettings() {
        return this.settings;
    }

    public void setSettings(UserSettings userSettings) {
        this.settings = userSettings;
        this.settingsId = userSettings != null ? userSettings.getId() : null;
    }

    public UserProfile settings(UserSettings userSettings) {
        this.setSettings(userSettings);
        return this;
    }

    public Set<UserReport> getMakedReports() {
        return this.makedReports;
    }

    public void setMakedReports(Set<UserReport> userReports) {
        if (this.makedReports != null) {
            this.makedReports.forEach(i -> i.setReporter(null));
        }
        if (userReports != null) {
            userReports.forEach(i -> i.setReporter(this));
        }
        this.makedReports = userReports;
    }

    public UserProfile makedReports(Set<UserReport> userReports) {
        this.setMakedReports(userReports);
        return this;
    }

    public UserProfile addMakedReports(UserReport userReport) {
        this.makedReports.add(userReport);
        userReport.setReporter(this);
        return this;
    }

    public UserProfile removeMakedReports(UserReport userReport) {
        this.makedReports.remove(userReport);
        userReport.setReporter(null);
        return this;
    }

    public Set<UserReport> getReceivedReports() {
        return this.receivedReports;
    }

    public void setReceivedReports(Set<UserReport> userReports) {
        if (this.receivedReports != null) {
            this.receivedReports.forEach(i -> i.setReported(null));
        }
        if (userReports != null) {
            userReports.forEach(i -> i.setReported(this));
        }
        this.receivedReports = userReports;
    }

    public UserProfile receivedReports(Set<UserReport> userReports) {
        this.setReceivedReports(userReports);
        return this;
    }

    public UserProfile addReceivedReports(UserReport userReport) {
        this.receivedReports.add(userReport);
        userReport.setReported(this);
        return this;
    }

    public UserProfile removeReceivedReports(UserReport userReport) {
        this.receivedReports.remove(userReport);
        userReport.setReported(null);
        return this;
    }

    public Set<AssistanceTicket> getAssistanceTickets() {
        return this.assistanceTickets;
    }

    public void setAssistanceTickets(Set<AssistanceTicket> assistanceTickets) {
        if (this.assistanceTickets != null) {
            this.assistanceTickets.forEach(i -> i.setUser(null));
        }
        if (assistanceTickets != null) {
            assistanceTickets.forEach(i -> i.setUser(this));
        }
        this.assistanceTickets = assistanceTickets;
    }

    public UserProfile assistanceTickets(Set<AssistanceTicket> assistanceTickets) {
        this.setAssistanceTickets(assistanceTickets);
        return this;
    }

    public UserProfile addAssistanceTickets(AssistanceTicket assistanceTicket) {
        this.assistanceTickets.add(assistanceTicket);
        assistanceTicket.setUser(this);
        return this;
    }

    public UserProfile removeAssistanceTickets(AssistanceTicket assistanceTicket) {
        this.assistanceTickets.remove(assistanceTicket);
        assistanceTicket.setUser(null);
        return this;
    }

    public Set<MoneyPayout> getWithdraws() {
        return this.withdraws;
    }

    public void setWithdraws(Set<MoneyPayout> moneyPayouts) {
        if (this.withdraws != null) {
            this.withdraws.forEach(i -> i.setCreator(null));
        }
        if (moneyPayouts != null) {
            moneyPayouts.forEach(i -> i.setCreator(this));
        }
        this.withdraws = moneyPayouts;
    }

    public UserProfile withdraws(Set<MoneyPayout> moneyPayouts) {
        this.setWithdraws(moneyPayouts);
        return this;
    }

    public UserProfile addWithdraws(MoneyPayout moneyPayout) {
        this.withdraws.add(moneyPayout);
        moneyPayout.setCreator(this);
        return this;
    }

    public UserProfile removeWithdraws(MoneyPayout moneyPayout) {
        this.withdraws.remove(moneyPayout);
        moneyPayout.setCreator(null);
        return this;
    }

    public Set<SubscriptionBundle> getSubscriptionBundles() {
        return this.subscriptionBundles;
    }

    public void setSubscriptionBundles(Set<SubscriptionBundle> subscriptionBundles) {
        if (this.subscriptionBundles != null) {
            this.subscriptionBundles.forEach(i -> i.setCreator(null));
        }
        if (subscriptionBundles != null) {
            subscriptionBundles.forEach(i -> i.setCreator(this));
        }
        this.subscriptionBundles = subscriptionBundles;
    }

    public UserProfile subscriptionBundles(Set<SubscriptionBundle> subscriptionBundles) {
        this.setSubscriptionBundles(subscriptionBundles);
        return this;
    }

    public UserProfile addSubscriptionBundles(SubscriptionBundle subscriptionBundle) {
        this.subscriptionBundles.add(subscriptionBundle);
        subscriptionBundle.setCreator(this);
        return this;
    }

    public UserProfile removeSubscriptionBundles(SubscriptionBundle subscriptionBundle) {
        this.subscriptionBundles.remove(subscriptionBundle);
        subscriptionBundle.setCreator(null);
        return this;
    }

    public Set<CreatorEarning> getEarnings() {
        return this.earnings;
    }

    public void setEarnings(Set<CreatorEarning> creatorEarnings) {
        if (this.earnings != null) {
            this.earnings.forEach(i -> i.setCreator(null));
        }
        if (creatorEarnings != null) {
            creatorEarnings.forEach(i -> i.setCreator(this));
        }
        this.earnings = creatorEarnings;
    }

    public UserProfile earnings(Set<CreatorEarning> creatorEarnings) {
        this.setEarnings(creatorEarnings);
        return this;
    }

    public UserProfile addEarnings(CreatorEarning creatorEarning) {
        this.earnings.add(creatorEarning);
        creatorEarning.setCreator(this);
        return this;
    }

    public UserProfile removeEarnings(CreatorEarning creatorEarning) {
        this.earnings.remove(creatorEarning);
        creatorEarning.setCreator(null);
        return this;
    }

    public Set<PaymentTransaction> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<PaymentTransaction> paymentTransactions) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setViewer(null));
        }
        if (paymentTransactions != null) {
            paymentTransactions.forEach(i -> i.setViewer(this));
        }
        this.payments = paymentTransactions;
    }

    public UserProfile payments(Set<PaymentTransaction> paymentTransactions) {
        this.setPayments(paymentTransactions);
        return this;
    }

    public UserProfile addPayments(PaymentTransaction paymentTransaction) {
        this.payments.add(paymentTransaction);
        paymentTransaction.setViewer(this);
        return this;
    }

    public UserProfile removePayments(PaymentTransaction paymentTransaction) {
        this.payments.remove(paymentTransaction);
        paymentTransaction.setViewer(null);
        return this;
    }

    public Set<OfferPromotion> getPlanOffers() {
        return this.planOffers;
    }

    public void setPlanOffers(Set<OfferPromotion> offerPromotions) {
        if (this.planOffers != null) {
            this.planOffers.forEach(i -> i.setCreator(null));
        }
        if (offerPromotions != null) {
            offerPromotions.forEach(i -> i.setCreator(this));
        }
        this.planOffers = offerPromotions;
    }

    public UserProfile planOffers(Set<OfferPromotion> offerPromotions) {
        this.setPlanOffers(offerPromotions);
        return this;
    }

    public UserProfile addPlanOffers(OfferPromotion offerPromotion) {
        this.planOffers.add(offerPromotion);
        offerPromotion.setCreator(this);
        return this;
    }

    public UserProfile removePlanOffers(OfferPromotion offerPromotion) {
        this.planOffers.remove(offerPromotion);
        offerPromotion.setCreator(null);
        return this;
    }

    public Set<PurchasedContent> getPurchasedContents() {
        return this.purchasedContents;
    }

    public void setPurchasedContents(Set<PurchasedContent> purchasedContents) {
        if (this.purchasedContents != null) {
            this.purchasedContents.forEach(i -> i.setViewer(null));
        }
        if (purchasedContents != null) {
            purchasedContents.forEach(i -> i.setViewer(this));
        }
        this.purchasedContents = purchasedContents;
    }

    public UserProfile purchasedContents(Set<PurchasedContent> purchasedContents) {
        this.setPurchasedContents(purchasedContents);
        return this;
    }

    public UserProfile addPurchasedContent(PurchasedContent purchasedContent) {
        this.purchasedContents.add(purchasedContent);
        purchasedContent.setViewer(this);
        return this;
    }

    public UserProfile removePurchasedContent(PurchasedContent purchasedContent) {
        this.purchasedContents.remove(purchasedContent);
        purchasedContent.setViewer(null);
        return this;
    }

    public Set<PurchasedSubscription> getPurchasedSubscriptions() {
        return this.purchasedSubscriptions;
    }

    public void setPurchasedSubscriptions(Set<PurchasedSubscription> purchasedSubscriptions) {
        if (this.purchasedSubscriptions != null) {
            this.purchasedSubscriptions.forEach(i -> i.setViewer(null));
        }
        if (purchasedSubscriptions != null) {
            purchasedSubscriptions.forEach(i -> i.setViewer(this));
        }
        this.purchasedSubscriptions = purchasedSubscriptions;
    }

    public UserProfile purchasedSubscriptions(Set<PurchasedSubscription> purchasedSubscriptions) {
        this.setPurchasedSubscriptions(purchasedSubscriptions);
        return this;
    }

    public UserProfile addPurchasedSubscriptions(PurchasedSubscription purchasedSubscription) {
        this.purchasedSubscriptions.add(purchasedSubscription);
        purchasedSubscription.setViewer(this);
        return this;
    }

    public UserProfile removePurchasedSubscriptions(PurchasedSubscription purchasedSubscription) {
        this.purchasedSubscriptions.remove(purchasedSubscription);
        purchasedSubscription.setViewer(null);
        return this;
    }

    public Set<WalletTransaction> getWallets() {
        return this.wallets;
    }

    public void setWallets(Set<WalletTransaction> walletTransactions) {
        if (this.wallets != null) {
            this.wallets.forEach(i -> i.setViewer(null));
        }
        if (walletTransactions != null) {
            walletTransactions.forEach(i -> i.setViewer(this));
        }
        this.wallets = walletTransactions;
    }

    public UserProfile wallets(Set<WalletTransaction> walletTransactions) {
        this.setWallets(walletTransactions);
        return this;
    }

    public UserProfile addWallet(WalletTransaction walletTransaction) {
        this.wallets.add(walletTransaction);
        walletTransaction.setViewer(this);
        return this;
    }

    public UserProfile removeWallet(WalletTransaction walletTransaction) {
        this.wallets.remove(walletTransaction);
        walletTransaction.setViewer(null);
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

    public Set<Notification> getCommentNotifications() {
        return this.commentNotifications;
    }

    public void setCommentNotifications(Set<Notification> notifications) {
        if (this.commentNotifications != null) {
            this.commentNotifications.forEach(i -> i.setCommentedUser(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setCommentedUser(this));
        }
        this.commentNotifications = notifications;
    }

    public UserProfile commentNotifications(Set<Notification> notifications) {
        this.setCommentNotifications(notifications);
        return this;
    }

    public UserProfile addCommentNotifications(Notification notification) {
        this.commentNotifications.add(notification);
        notification.setCommentedUser(this);
        return this;
    }

    public UserProfile removeCommentNotifications(Notification notification) {
        this.commentNotifications.remove(notification);
        notification.setCommentedUser(null);
        return this;
    }

    public Set<Notification> getMessageNotifications() {
        return this.messageNotifications;
    }

    public void setMessageNotifications(Set<Notification> notifications) {
        if (this.messageNotifications != null) {
            this.messageNotifications.forEach(i -> i.setMessagedUser(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setMessagedUser(this));
        }
        this.messageNotifications = notifications;
    }

    public UserProfile messageNotifications(Set<Notification> notifications) {
        this.setMessageNotifications(notifications);
        return this;
    }

    public UserProfile addMessageNotifications(Notification notification) {
        this.messageNotifications.add(notification);
        notification.setMessagedUser(this);
        return this;
    }

    public UserProfile removeMessageNotifications(Notification notification) {
        this.messageNotifications.remove(notification);
        notification.setMessagedUser(null);
        return this;
    }

    public Set<Notification> getUserMentionNotifications() {
        return this.userMentionNotifications;
    }

    public void setUserMentionNotifications(Set<Notification> notifications) {
        if (this.userMentionNotifications != null) {
            this.userMentionNotifications.forEach(i -> i.setMentionerUserInPost(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setMentionerUserInPost(this));
        }
        this.userMentionNotifications = notifications;
    }

    public UserProfile userMentionNotifications(Set<Notification> notifications) {
        this.setUserMentionNotifications(notifications);
        return this;
    }

    public UserProfile addUserMentionNotifications(Notification notification) {
        this.userMentionNotifications.add(notification);
        notification.setMentionerUserInPost(this);
        return this;
    }

    public UserProfile removeUserMentionNotifications(Notification notification) {
        this.userMentionNotifications.remove(notification);
        notification.setMentionerUserInPost(null);
        return this;
    }

    public Set<Notification> getCommentMentionNotifications() {
        return this.commentMentionNotifications;
    }

    public void setCommentMentionNotifications(Set<Notification> notifications) {
        if (this.commentMentionNotifications != null) {
            this.commentMentionNotifications.forEach(i -> i.setMentionerUserInComment(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setMentionerUserInComment(this));
        }
        this.commentMentionNotifications = notifications;
    }

    public UserProfile commentMentionNotifications(Set<Notification> notifications) {
        this.setCommentMentionNotifications(notifications);
        return this;
    }

    public UserProfile addCommentMentionNotifications(Notification notification) {
        this.commentMentionNotifications.add(notification);
        notification.setMentionerUserInComment(this);
        return this;
    }

    public UserProfile removeCommentMentionNotifications(Notification notification) {
        this.commentMentionNotifications.remove(notification);
        notification.setMentionerUserInComment(null);
        return this;
    }

    public Set<UserAssociation> getOwnAccountsAssociations() {
        return this.ownAccountsAssociations;
    }

    public void setOwnAccountsAssociations(Set<UserAssociation> userAssociations) {
        if (this.ownAccountsAssociations != null) {
            this.ownAccountsAssociations.forEach(i -> i.setOwner(null));
        }
        if (userAssociations != null) {
            userAssociations.forEach(i -> i.setOwner(this));
        }
        this.ownAccountsAssociations = userAssociations;
    }

    public UserProfile ownAccountsAssociations(Set<UserAssociation> userAssociations) {
        this.setOwnAccountsAssociations(userAssociations);
        return this;
    }

    public UserProfile addOwnAccountsAssociations(UserAssociation userAssociation) {
        this.ownAccountsAssociations.add(userAssociation);
        userAssociation.setOwner(this);
        return this;
    }

    public UserProfile removeOwnAccountsAssociations(UserAssociation userAssociation) {
        this.ownAccountsAssociations.remove(userAssociation);
        userAssociation.setOwner(null);
        return this;
    }

    public Set<UserEvent> getCreatedEvents() {
        return this.createdEvents;
    }

    public void setCreatedEvents(Set<UserEvent> userEvents) {
        if (this.createdEvents != null) {
            this.createdEvents.forEach(i -> i.setCreator(null));
        }
        if (userEvents != null) {
            userEvents.forEach(i -> i.setCreator(this));
        }
        this.createdEvents = userEvents;
    }

    public UserProfile createdEvents(Set<UserEvent> userEvents) {
        this.setCreatedEvents(userEvents);
        return this;
    }

    public UserProfile addCreatedEvents(UserEvent userEvent) {
        this.createdEvents.add(userEvent);
        userEvent.setCreator(this);
        return this;
    }

    public UserProfile removeCreatedEvents(UserEvent userEvent) {
        this.createdEvents.remove(userEvent);
        userEvent.setCreator(null);
        return this;
    }

    public Set<BookMark> getBookMarks() {
        return this.bookMarks;
    }

    public void setBookMarks(Set<BookMark> bookMarks) {
        if (this.bookMarks != null) {
            this.bookMarks.forEach(i -> i.setUser(null));
        }
        if (bookMarks != null) {
            bookMarks.forEach(i -> i.setUser(this));
        }
        this.bookMarks = bookMarks;
    }

    public UserProfile bookMarks(Set<BookMark> bookMarks) {
        this.setBookMarks(bookMarks);
        return this;
    }

    public UserProfile addBookMarks(BookMark bookMark) {
        this.bookMarks.add(bookMark);
        bookMark.setUser(this);
        return this;
    }

    public UserProfile removeBookMarks(BookMark bookMark) {
        this.bookMarks.remove(bookMark);
        bookMark.setUser(null);
        return this;
    }

    public Set<Feedback> getFeedbacks() {
        return this.feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        if (this.feedbacks != null) {
            this.feedbacks.forEach(i -> i.setCreator(null));
        }
        if (feedbacks != null) {
            feedbacks.forEach(i -> i.setCreator(this));
        }
        this.feedbacks = feedbacks;
    }

    public UserProfile feedbacks(Set<Feedback> feedbacks) {
        this.setFeedbacks(feedbacks);
        return this;
    }

    public UserProfile addFeedback(Feedback feedback) {
        this.feedbacks.add(feedback);
        feedback.setCreator(this);
        return this;
    }

    public UserProfile removeFeedback(Feedback feedback) {
        this.feedbacks.remove(feedback);
        feedback.setCreator(null);
        return this;
    }

    public Set<DirectMessage> getSentMessages() {
        return this.sentMessages;
    }

    public void setSentMessages(Set<DirectMessage> directMessages) {
        if (this.sentMessages != null) {
            this.sentMessages.forEach(i -> i.setUser(null));
        }
        if (directMessages != null) {
            directMessages.forEach(i -> i.setUser(this));
        }
        this.sentMessages = directMessages;
    }

    public UserProfile sentMessages(Set<DirectMessage> directMessages) {
        this.setSentMessages(directMessages);
        return this;
    }

    public UserProfile addSentMessages(DirectMessage directMessage) {
        this.sentMessages.add(directMessage);
        directMessage.setUser(this);
        return this;
    }

    public UserProfile removeSentMessages(DirectMessage directMessage) {
        this.sentMessages.remove(directMessage);
        directMessage.setUser(null);
        return this;
    }

    public Set<ChatRoom> getChats() {
        return this.chats;
    }

    public void setChats(Set<ChatRoom> chatRooms) {
        if (this.chats != null) {
            this.chats.forEach(i -> i.setUser(null));
        }
        if (chatRooms != null) {
            chatRooms.forEach(i -> i.setUser(this));
        }
        this.chats = chatRooms;
    }

    public UserProfile chats(Set<ChatRoom> chatRooms) {
        this.setChats(chatRooms);
        return this;
    }

    public UserProfile addChats(ChatRoom chatRoom) {
        this.chats.add(chatRoom);
        chatRoom.setUser(this);
        return this;
    }

    public UserProfile removeChats(ChatRoom chatRoom) {
        this.chats.remove(chatRoom);
        chatRoom.setUser(null);
        return this;
    }

    public Set<UserMention> getMentions() {
        return this.mentions;
    }

    public void setMentions(Set<UserMention> userMentions) {
        if (this.mentions != null) {
            this.mentions.forEach(i -> i.setMentionedUser(null));
        }
        if (userMentions != null) {
            userMentions.forEach(i -> i.setMentionedUser(this));
        }
        this.mentions = userMentions;
    }

    public UserProfile mentions(Set<UserMention> userMentions) {
        this.setMentions(userMentions);
        return this;
    }

    public UserProfile addMentions(UserMention userMention) {
        this.mentions.add(userMention);
        userMention.setMentionedUser(this);
        return this;
    }

    public UserProfile removeMentions(UserMention userMention) {
        this.mentions.remove(userMention);
        userMention.setMentionedUser(null);
        return this;
    }

    public Set<PostComment> getComments() {
        return this.comments;
    }

    public void setComments(Set<PostComment> postComments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setCommenter(null));
        }
        if (postComments != null) {
            postComments.forEach(i -> i.setCommenter(this));
        }
        this.comments = postComments;
    }

    public UserProfile comments(Set<PostComment> postComments) {
        this.setComments(postComments);
        return this;
    }

    public UserProfile addComments(PostComment postComment) {
        this.comments.add(postComment);
        postComment.setCommenter(this);
        return this;
    }

    public UserProfile removeComments(PostComment postComment) {
        this.comments.remove(postComment);
        postComment.setCommenter(null);
        return this;
    }

    public Set<PostFeed> getFeeds() {
        return this.feeds;
    }

    public void setFeeds(Set<PostFeed> postFeeds) {
        if (this.feeds != null) {
            this.feeds.forEach(i -> i.setCreator(null));
        }
        if (postFeeds != null) {
            postFeeds.forEach(i -> i.setCreator(this));
        }
        this.feeds = postFeeds;
    }

    public UserProfile feeds(Set<PostFeed> postFeeds) {
        this.setFeeds(postFeeds);
        return this;
    }

    public UserProfile addFeeds(PostFeed postFeed) {
        this.feeds.add(postFeed);
        postFeed.setCreator(this);
        return this;
    }

    public UserProfile removeFeeds(PostFeed postFeed) {
        this.feeds.remove(postFeed);
        postFeed.setCreator(null);
        return this;
    }

    public Set<PollVote> getVotedPolls() {
        return this.votedPolls;
    }

    public void setVotedPolls(Set<PollVote> pollVotes) {
        if (this.votedPolls != null) {
            this.votedPolls.forEach(i -> i.setVotingUser(null));
        }
        if (pollVotes != null) {
            pollVotes.forEach(i -> i.setVotingUser(this));
        }
        this.votedPolls = pollVotes;
    }

    public UserProfile votedPolls(Set<PollVote> pollVotes) {
        this.setVotedPolls(pollVotes);
        return this;
    }

    public UserProfile addVotedPolls(PollVote pollVote) {
        this.votedPolls.add(pollVote);
        pollVote.setVotingUser(this);
        return this;
    }

    public UserProfile removeVotedPolls(PollVote pollVote) {
        this.votedPolls.remove(pollVote);
        pollVote.setVotingUser(null);
        return this;
    }

    public Set<VideoStory> getVideoStories() {
        return this.videoStories;
    }

    public void setVideoStories(Set<VideoStory> videoStories) {
        if (this.videoStories != null) {
            this.videoStories.forEach(i -> i.setCreator(null));
        }
        if (videoStories != null) {
            videoStories.forEach(i -> i.setCreator(this));
        }
        this.videoStories = videoStories;
    }

    public UserProfile videoStories(Set<VideoStory> videoStories) {
        this.setVideoStories(videoStories);
        return this;
    }

    public UserProfile addVideoStories(VideoStory videoStory) {
        this.videoStories.add(videoStory);
        videoStory.setCreator(this);
        return this;
    }

    public UserProfile removeVideoStories(VideoStory videoStory) {
        this.videoStories.remove(videoStory);
        videoStory.setCreator(null);
        return this;
    }

    public Set<SingleDocument> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<SingleDocument> singleDocuments) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setUser(null));
        }
        if (singleDocuments != null) {
            singleDocuments.forEach(i -> i.setUser(this));
        }
        this.documents = singleDocuments;
    }

    public UserProfile documents(Set<SingleDocument> singleDocuments) {
        this.setDocuments(singleDocuments);
        return this;
    }

    public UserProfile addDocuments(SingleDocument singleDocument) {
        this.documents.add(singleDocument);
        singleDocument.setUser(this);
        return this;
    }

    public UserProfile removeDocuments(SingleDocument singleDocument) {
        this.documents.remove(singleDocument);
        singleDocument.setUser(null);
        return this;
    }

    public Country getCountryOfBirth() {
        return this.countryOfBirth;
    }

    public void setCountryOfBirth(Country country) {
        this.countryOfBirth = country;
        this.countryOfBirthId = country != null ? country.getId() : null;
    }

    public UserProfile countryOfBirth(Country country) {
        this.setCountryOfBirth(country);
        return this;
    }

    public State getStateOfResidence() {
        return this.stateOfResidence;
    }

    public void setStateOfResidence(State state) {
        this.stateOfResidence = state;
        this.stateOfResidenceId = state != null ? state.getId() : null;
    }

    public UserProfile stateOfResidence(State state) {
        this.setStateOfResidence(state);
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

    public Set<State> getBlockedUbications() {
        return this.blockedUbications;
    }

    public void setBlockedUbications(Set<State> states) {
        this.blockedUbications = states;
    }

    public UserProfile blockedUbications(Set<State> states) {
        this.setBlockedUbications(states);
        return this;
    }

    public UserProfile addBlockedUbications(State state) {
        this.blockedUbications.add(state);
        return this;
    }

    public UserProfile removeBlockedUbications(State state) {
        this.blockedUbications.remove(state);
        return this;
    }

    public Set<HashTag> getHashTags() {
        return this.hashTags;
    }

    public void setHashTags(Set<HashTag> hashTags) {
        this.hashTags = hashTags;
    }

    public UserProfile hashTags(Set<HashTag> hashTags) {
        this.setHashTags(hashTags);
        return this;
    }

    public UserProfile addHashTags(HashTag hashTag) {
        this.hashTags.add(hashTag);
        return this;
    }

    public UserProfile removeHashTags(HashTag hashTag) {
        this.hashTags.remove(hashTag);
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

    public Set<ContentPackage> getContentPackageTags() {
        return this.contentPackageTags;
    }

    public void setContentPackageTags(Set<ContentPackage> contentPackages) {
        if (this.contentPackageTags != null) {
            this.contentPackageTags.forEach(i -> i.removeUsersTagged(this));
        }
        if (contentPackages != null) {
            contentPackages.forEach(i -> i.addUsersTagged(this));
        }
        this.contentPackageTags = contentPackages;
    }

    public UserProfile contentPackageTags(Set<ContentPackage> contentPackages) {
        this.setContentPackageTags(contentPackages);
        return this;
    }

    public UserProfile addContentPackageTags(ContentPackage contentPackage) {
        this.contentPackageTags.add(contentPackage);
        contentPackage.getUsersTaggeds().add(this);
        return this;
    }

    public UserProfile removeContentPackageTags(ContentPackage contentPackage) {
        this.contentPackageTags.remove(contentPackage);
        contentPackage.getUsersTaggeds().remove(this);
        return this;
    }

    public Long getUserLiteId() {
        return this.userLiteId;
    }

    public void setUserLiteId(Long userLite) {
        this.userLiteId = userLite;
    }

    public Long getSettingsId() {
        return this.settingsId;
    }

    public void setSettingsId(Long userSettings) {
        this.settingsId = userSettings;
    }

    public Long getCountryOfBirthId() {
        return this.countryOfBirthId;
    }

    public void setCountryOfBirthId(Long country) {
        this.countryOfBirthId = country;
    }

    public Long getStateOfResidenceId() {
        return this.stateOfResidenceId;
    }

    public void setStateOfResidenceId(Long state) {
        this.stateOfResidenceId = state;
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
            "}";
    }
}
