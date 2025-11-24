package com.monsterdam.admin.domain;

import com.monsterdam.admin.domain.enumeration.AdminGender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AdminUserProfile.
 */
@Table("admin_user_profile")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "adminuserprofile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdminUserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[a-z0-9_-]+$")
    @Column("full_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fullName;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$")
    @Column("email_address")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String emailAddress;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$")
    @Column("nick_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nickName;

    @NotNull(message = "must not be null")
    @Column("gender")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AdminGender gender;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    @Column("mobile_phone")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String mobilePhone;

    @NotNull(message = "must not be null")
    @Column("last_login_date")
    private Instant lastLoginDate;

    @NotNull(message = "must not be null")
    @Column("birth_date")
    private LocalDate birthDate;

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
    @JsonIgnoreProperties(value = { "moderationAction", "report", "documentsReview", "assignedAdmin", "user" }, allowSetters = true)
    private Set<AssistanceTicket> assignedTickets = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "announcerMessage", "admin" }, allowSetters = true)
    private Set<AdminAnnouncement> announcements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AdminUserProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public AdminUserProfile fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public AdminUserProfile emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNickName() {
        return this.nickName;
    }

    public AdminUserProfile nickName(String nickName) {
        this.setNickName(nickName);
        return this;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public AdminGender getGender() {
        return this.gender;
    }

    public AdminUserProfile gender(AdminGender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(AdminGender gender) {
        this.gender = gender;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public AdminUserProfile mobilePhone(String mobilePhone) {
        this.setMobilePhone(mobilePhone);
        return this;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Instant getLastLoginDate() {
        return this.lastLoginDate;
    }

    public AdminUserProfile lastLoginDate(Instant lastLoginDate) {
        this.setLastLoginDate(lastLoginDate);
        return this;
    }

    public void setLastLoginDate(Instant lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public AdminUserProfile birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public AdminUserProfile createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public AdminUserProfile lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public AdminUserProfile createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public AdminUserProfile lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public AdminUserProfile isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<AssistanceTicket> getAssignedTickets() {
        return this.assignedTickets;
    }

    public void setAssignedTickets(Set<AssistanceTicket> assistanceTickets) {
        if (this.assignedTickets != null) {
            this.assignedTickets.forEach(i -> i.setAssignedAdmin(null));
        }
        if (assistanceTickets != null) {
            assistanceTickets.forEach(i -> i.setAssignedAdmin(this));
        }
        this.assignedTickets = assistanceTickets;
    }

    public AdminUserProfile assignedTickets(Set<AssistanceTicket> assistanceTickets) {
        this.setAssignedTickets(assistanceTickets);
        return this;
    }

    public AdminUserProfile addAssignedTickets(AssistanceTicket assistanceTicket) {
        this.assignedTickets.add(assistanceTicket);
        assistanceTicket.setAssignedAdmin(this);
        return this;
    }

    public AdminUserProfile removeAssignedTickets(AssistanceTicket assistanceTicket) {
        this.assignedTickets.remove(assistanceTicket);
        assistanceTicket.setAssignedAdmin(null);
        return this;
    }

    public Set<AdminAnnouncement> getAnnouncements() {
        return this.announcements;
    }

    public void setAnnouncements(Set<AdminAnnouncement> adminAnnouncements) {
        if (this.announcements != null) {
            this.announcements.forEach(i -> i.setAdmin(null));
        }
        if (adminAnnouncements != null) {
            adminAnnouncements.forEach(i -> i.setAdmin(this));
        }
        this.announcements = adminAnnouncements;
    }

    public AdminUserProfile announcements(Set<AdminAnnouncement> adminAnnouncements) {
        this.setAnnouncements(adminAnnouncements);
        return this;
    }

    public AdminUserProfile addAnnouncements(AdminAnnouncement adminAnnouncement) {
        this.announcements.add(adminAnnouncement);
        adminAnnouncement.setAdmin(this);
        return this;
    }

    public AdminUserProfile removeAnnouncements(AdminAnnouncement adminAnnouncement) {
        this.announcements.remove(adminAnnouncement);
        adminAnnouncement.setAdmin(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminUserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((AdminUserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminUserProfile{" +
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
