package com.fanflip.profile.domain;

import com.fanflip.profile.domain.enumeration.ContentPreference;
import com.fanflip.profile.domain.enumeration.UserGender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserLite.
 */
@Entity
@Table(name = "user_lite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserLite implements Serializable {

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

    @Column(name = "thumbnail_s_3_key")
    private String thumbnailS3Key;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private UserGender gender;

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

    @NotNull
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$")
    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "content_preference", nullable = false)
    private ContentPreference contentPreference;

    @Column(name = "country_of_birth_id")
    private Long countryOfBirthId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserLite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getThumbnail() {
        return this.thumbnail;
    }

    public UserLite thumbnail(byte[] thumbnail) {
        this.setThumbnail(thumbnail);
        return this;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return this.thumbnailContentType;
    }

    public UserLite thumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
        return this;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public String getThumbnailS3Key() {
        return this.thumbnailS3Key;
    }

    public UserLite thumbnailS3Key(String thumbnailS3Key) {
        this.setThumbnailS3Key(thumbnailS3Key);
        return this;
    }

    public void setThumbnailS3Key(String thumbnailS3Key) {
        this.thumbnailS3Key = thumbnailS3Key;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public UserLite birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public UserGender getGender() {
        return this.gender;
    }

    public UserLite gender(UserGender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(UserGender gender) {
        this.gender = gender;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserLite createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserLite lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserLite createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserLite lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public UserLite isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getNickName() {
        return this.nickName;
    }

    public UserLite nickName(String nickName) {
        this.setNickName(nickName);
        return this;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public UserLite fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ContentPreference getContentPreference() {
        return this.contentPreference;
    }

    public UserLite contentPreference(ContentPreference contentPreference) {
        this.setContentPreference(contentPreference);
        return this;
    }

    public void setContentPreference(ContentPreference contentPreference) {
        this.contentPreference = contentPreference;
    }

    public Long getCountryOfBirthId() {
        return this.countryOfBirthId;
    }

    public UserLite countryOfBirthId(Long countryOfBirthId) {
        this.setCountryOfBirthId(countryOfBirthId);
        return this;
    }

    public void setCountryOfBirthId(Long countryOfBirthId) {
        this.countryOfBirthId = countryOfBirthId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserLite)) {
            return false;
        }
        return getId() != null && getId().equals(((UserLite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserLite{" +
            "id=" + getId() +
            ", thumbnail='" + getThumbnail() + "'" +
            ", thumbnailContentType='" + getThumbnailContentType() + "'" +
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
