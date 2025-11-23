package com.fanflip.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ContentPackage.
 */
@Table("content_package")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "contentpackage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContentPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("amount")
    private BigDecimal amount;

    @Column("video_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer videoCount;

    @Column("image_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer imageCount;

    @NotNull(message = "must not be null")
    @Column("is_paid_content")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isPaidContent;

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
    private SingleAudio audio;

    @Transient
    @JsonIgnoreProperties(
        value = { "payment", "walletTransaction", "creatorEarning", "viewer", "purchasedContentPackage" },
        allowSetters = true
    )
    private Set<PurchasedContent> selledPackages = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "reports", "belongPackage" }, allowSetters = true)
    private Set<SingleVideo> videos = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "reports", "belongPackage" }, allowSetters = true)
    private Set<SinglePhoto> photos = new HashSet<>();

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
    private Set<UserProfile> usersTaggeds = new HashSet<>();

    @Transient
    private DirectMessage message;

    @Transient
    private PostFeed post;

    @Column("audio_id")
    private Long audioId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContentPackage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public ContentPackage amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount != null ? amount.stripTrailingZeros() : null;
    }

    public Integer getVideoCount() {
        return this.videoCount;
    }

    public ContentPackage videoCount(Integer videoCount) {
        this.setVideoCount(videoCount);
        return this;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }

    public Integer getImageCount() {
        return this.imageCount;
    }

    public ContentPackage imageCount(Integer imageCount) {
        this.setImageCount(imageCount);
        return this;
    }

    public void setImageCount(Integer imageCount) {
        this.imageCount = imageCount;
    }

    public Boolean getIsPaidContent() {
        return this.isPaidContent;
    }

    public ContentPackage isPaidContent(Boolean isPaidContent) {
        this.setIsPaidContent(isPaidContent);
        return this;
    }

    public void setIsPaidContent(Boolean isPaidContent) {
        this.isPaidContent = isPaidContent;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ContentPackage createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ContentPackage lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ContentPackage createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public ContentPackage lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public ContentPackage isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public SingleAudio getAudio() {
        return this.audio;
    }

    public void setAudio(SingleAudio singleAudio) {
        this.audio = singleAudio;
        this.audioId = singleAudio != null ? singleAudio.getId() : null;
    }

    public ContentPackage audio(SingleAudio singleAudio) {
        this.setAudio(singleAudio);
        return this;
    }

    public Set<PurchasedContent> getSelledPackages() {
        return this.selledPackages;
    }

    public void setSelledPackages(Set<PurchasedContent> purchasedContents) {
        if (this.selledPackages != null) {
            this.selledPackages.forEach(i -> i.setPurchasedContentPackage(null));
        }
        if (purchasedContents != null) {
            purchasedContents.forEach(i -> i.setPurchasedContentPackage(this));
        }
        this.selledPackages = purchasedContents;
    }

    public ContentPackage selledPackages(Set<PurchasedContent> purchasedContents) {
        this.setSelledPackages(purchasedContents);
        return this;
    }

    public ContentPackage addSelledPackages(PurchasedContent purchasedContent) {
        this.selledPackages.add(purchasedContent);
        purchasedContent.setPurchasedContentPackage(this);
        return this;
    }

    public ContentPackage removeSelledPackages(PurchasedContent purchasedContent) {
        this.selledPackages.remove(purchasedContent);
        purchasedContent.setPurchasedContentPackage(null);
        return this;
    }

    public Set<SingleVideo> getVideos() {
        return this.videos;
    }

    public void setVideos(Set<SingleVideo> singleVideos) {
        if (this.videos != null) {
            this.videos.forEach(i -> i.setBelongPackage(null));
        }
        if (singleVideos != null) {
            singleVideos.forEach(i -> i.setBelongPackage(this));
        }
        this.videos = singleVideos;
    }

    public ContentPackage videos(Set<SingleVideo> singleVideos) {
        this.setVideos(singleVideos);
        return this;
    }

    public ContentPackage addVideos(SingleVideo singleVideo) {
        this.videos.add(singleVideo);
        singleVideo.setBelongPackage(this);
        return this;
    }

    public ContentPackage removeVideos(SingleVideo singleVideo) {
        this.videos.remove(singleVideo);
        singleVideo.setBelongPackage(null);
        return this;
    }

    public Set<SinglePhoto> getPhotos() {
        return this.photos;
    }

    public void setPhotos(Set<SinglePhoto> singlePhotos) {
        if (this.photos != null) {
            this.photos.forEach(i -> i.setBelongPackage(null));
        }
        if (singlePhotos != null) {
            singlePhotos.forEach(i -> i.setBelongPackage(this));
        }
        this.photos = singlePhotos;
    }

    public ContentPackage photos(Set<SinglePhoto> singlePhotos) {
        this.setPhotos(singlePhotos);
        return this;
    }

    public ContentPackage addPhotos(SinglePhoto singlePhoto) {
        this.photos.add(singlePhoto);
        singlePhoto.setBelongPackage(this);
        return this;
    }

    public ContentPackage removePhotos(SinglePhoto singlePhoto) {
        this.photos.remove(singlePhoto);
        singlePhoto.setBelongPackage(null);
        return this;
    }

    public Set<UserProfile> getUsersTaggeds() {
        return this.usersTaggeds;
    }

    public void setUsersTaggeds(Set<UserProfile> userProfiles) {
        this.usersTaggeds = userProfiles;
    }

    public ContentPackage usersTaggeds(Set<UserProfile> userProfiles) {
        this.setUsersTaggeds(userProfiles);
        return this;
    }

    public ContentPackage addUsersTagged(UserProfile userProfile) {
        this.usersTaggeds.add(userProfile);
        return this;
    }

    public ContentPackage removeUsersTagged(UserProfile userProfile) {
        this.usersTaggeds.remove(userProfile);
        return this;
    }

    public DirectMessage getMessage() {
        return this.message;
    }

    public void setMessage(DirectMessage directMessage) {
        if (this.message != null) {
            this.message.setContentPackage(null);
        }
        if (directMessage != null) {
            directMessage.setContentPackage(this);
        }
        this.message = directMessage;
    }

    public ContentPackage message(DirectMessage directMessage) {
        this.setMessage(directMessage);
        return this;
    }

    public PostFeed getPost() {
        return this.post;
    }

    public void setPost(PostFeed postFeed) {
        if (this.post != null) {
            this.post.setContentPackage(null);
        }
        if (postFeed != null) {
            postFeed.setContentPackage(this);
        }
        this.post = postFeed;
    }

    public ContentPackage post(PostFeed postFeed) {
        this.setPost(postFeed);
        return this;
    }

    public Long getAudioId() {
        return this.audioId;
    }

    public void setAudioId(Long singleAudio) {
        this.audioId = singleAudio;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentPackage)) {
            return false;
        }
        return getId() != null && getId().equals(((ContentPackage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContentPackage{" +
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
            "}";
    }
}
