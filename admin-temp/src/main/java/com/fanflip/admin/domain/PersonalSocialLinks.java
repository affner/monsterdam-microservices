package com.fanflip.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PersonalSocialLinks.
 */
@Table("personal_social_links")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "personalsociallinks")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonalSocialLinks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("thumbnail")
    private byte[] thumbnail;

    @Column("thumbnail_content_type")
    private String thumbnailContentType;

    @Column("normal_image")
    private byte[] normalImage;

    @Column("normal_image_content_type")
    private String normalImageContentType;

    @Column("normal_image_s_3_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String normalImageS3Key;

    @Column("thumbnail_icon_s_3_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String thumbnailIconS3Key;

    @NotNull(message = "must not be null")
    @Column("social_link")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String socialLink;

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
    private SocialNetwork socialNetwork;

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
    private UserProfile user;

    @Column("social_network_id")
    private Long socialNetworkId;

    @Column("user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PersonalSocialLinks id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getThumbnail() {
        return this.thumbnail;
    }

    public PersonalSocialLinks thumbnail(byte[] thumbnail) {
        this.setThumbnail(thumbnail);
        return this;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return this.thumbnailContentType;
    }

    public PersonalSocialLinks thumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
        return this;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public byte[] getNormalImage() {
        return this.normalImage;
    }

    public PersonalSocialLinks normalImage(byte[] normalImage) {
        this.setNormalImage(normalImage);
        return this;
    }

    public void setNormalImage(byte[] normalImage) {
        this.normalImage = normalImage;
    }

    public String getNormalImageContentType() {
        return this.normalImageContentType;
    }

    public PersonalSocialLinks normalImageContentType(String normalImageContentType) {
        this.normalImageContentType = normalImageContentType;
        return this;
    }

    public void setNormalImageContentType(String normalImageContentType) {
        this.normalImageContentType = normalImageContentType;
    }

    public String getNormalImageS3Key() {
        return this.normalImageS3Key;
    }

    public PersonalSocialLinks normalImageS3Key(String normalImageS3Key) {
        this.setNormalImageS3Key(normalImageS3Key);
        return this;
    }

    public void setNormalImageS3Key(String normalImageS3Key) {
        this.normalImageS3Key = normalImageS3Key;
    }

    public String getThumbnailIconS3Key() {
        return this.thumbnailIconS3Key;
    }

    public PersonalSocialLinks thumbnailIconS3Key(String thumbnailIconS3Key) {
        this.setThumbnailIconS3Key(thumbnailIconS3Key);
        return this;
    }

    public void setThumbnailIconS3Key(String thumbnailIconS3Key) {
        this.thumbnailIconS3Key = thumbnailIconS3Key;
    }

    public String getSocialLink() {
        return this.socialLink;
    }

    public PersonalSocialLinks socialLink(String socialLink) {
        this.setSocialLink(socialLink);
        return this;
    }

    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PersonalSocialLinks createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public PersonalSocialLinks lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PersonalSocialLinks createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PersonalSocialLinks lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public PersonalSocialLinks isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public SocialNetwork getSocialNetwork() {
        return this.socialNetwork;
    }

    public void setSocialNetwork(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
        this.socialNetworkId = socialNetwork != null ? socialNetwork.getId() : null;
    }

    public PersonalSocialLinks socialNetwork(SocialNetwork socialNetwork) {
        this.setSocialNetwork(socialNetwork);
        return this;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
        this.userId = userProfile != null ? userProfile.getId() : null;
    }

    public PersonalSocialLinks user(UserProfile userProfile) {
        this.setUser(userProfile);
        return this;
    }

    public Long getSocialNetworkId() {
        return this.socialNetworkId;
    }

    public void setSocialNetworkId(Long socialNetwork) {
        this.socialNetworkId = socialNetwork;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userProfile) {
        this.userId = userProfile;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonalSocialLinks)) {
            return false;
        }
        return getId() != null && getId().equals(((PersonalSocialLinks) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalSocialLinks{" +
            "id=" + getId() +
            ", thumbnail='" + getThumbnail() + "'" +
            ", thumbnailContentType='" + getThumbnailContentType() + "'" +
            ", normalImage='" + getNormalImage() + "'" +
            ", normalImageContentType='" + getNormalImageContentType() + "'" +
            ", normalImageS3Key='" + getNormalImageS3Key() + "'" +
            ", thumbnailIconS3Key='" + getThumbnailIconS3Key() + "'" +
            ", socialLink='" + getSocialLink() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
