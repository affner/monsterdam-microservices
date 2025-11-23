package com.fanflip.profile.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PersonalSocialLinks.
 */
@Entity
@Table(name = "personal_social_links")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonalSocialLinks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "thumbnail")
    private byte[] thumbnail;

    @Column(name = "thumbnail_content_type")
    private String thumbnailContentType;

    @Lob
    @Column(name = "normal_image")
    private byte[] normalImage;

    @Column(name = "normal_image_content_type")
    private String normalImageContentType;

    @Column(name = "normal_image_s_3_key")
    private String normalImageS3Key;

    @Column(name = "thumbnail_icon_s_3_key")
    private String thumbnailIconS3Key;

    @NotNull
    @Column(name = "social_link", nullable = false, unique = true)
    private String socialLink;

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

    @Column(name = "social_network_id")
    private Long socialNetworkId;

    @ManyToOne(optional = false)
    @NotNull
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
    private UserProfile user;

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

    public Long getSocialNetworkId() {
        return this.socialNetworkId;
    }

    public PersonalSocialLinks socialNetworkId(Long socialNetworkId) {
        this.setSocialNetworkId(socialNetworkId);
        return this;
    }

    public void setSocialNetworkId(Long socialNetworkId) {
        this.socialNetworkId = socialNetworkId;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
    }

    public PersonalSocialLinks user(UserProfile userProfile) {
        this.setUser(userProfile);
        return this;
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
            ", socialNetworkId=" + getSocialNetworkId() +
            "}";
    }
}
