package com.monsterdam.admin.service.dto;

import com.monsterdam.admin.domain.enumeration.AdminGender;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.AdminUserProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdminUserProfileDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[a-z0-9_-]+$")
    private String fullName;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$")
    private String emailAddress;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$")
    private String nickName;

    @NotNull(message = "must not be null")
    private AdminGender gender;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    private String mobilePhone;

    @NotNull(message = "must not be null")
    private Instant lastLoginDate;

    @NotNull(message = "must not be null")
    private LocalDate birthDate;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public AdminGender getGender() {
        return gender;
    }

    public void setGender(AdminGender gender) {
        this.gender = gender;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Instant getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Instant lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminUserProfileDTO)) {
            return false;
        }

        AdminUserProfileDTO adminUserProfileDTO = (AdminUserProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, adminUserProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminUserProfileDTO{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", nickName='" + getNickName() + "'" +
            ", gender='" + getGender() + "'" +
            ", mobilePhone='" + getMobilePhone() + "'" +
            ", lastLoginDate='" + getLastLoginDate() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
