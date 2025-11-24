package com.monsterdam.admin.domain;

import com.monsterdam.admin.domain.enumeration.AdminAnnouncementType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AdminAnnouncement.
 */
@Table("admin_announcement")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "adminannouncement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdminAnnouncement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("announcement_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AdminAnnouncementType announcementType;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("title")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @NotNull(message = "must not be null")
    @Column("content")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String content;

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

    @Transient
    private DirectMessage announcerMessage;

    @Transient
    @JsonIgnoreProperties(value = { "assignedTickets", "announcements" }, allowSetters = true)
    private AdminUserProfile admin;

    @Column("announcer_message_id")
    private Long announcerMessageId;

    @Column("admin_id")
    private Long adminId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AdminAnnouncement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdminAnnouncementType getAnnouncementType() {
        return this.announcementType;
    }

    public AdminAnnouncement announcementType(AdminAnnouncementType announcementType) {
        this.setAnnouncementType(announcementType);
        return this;
    }

    public void setAnnouncementType(AdminAnnouncementType announcementType) {
        this.announcementType = announcementType;
    }

    public String getTitle() {
        return this.title;
    }

    public AdminAnnouncement title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public AdminAnnouncement content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public AdminAnnouncement createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public AdminAnnouncement lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public AdminAnnouncement createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public AdminAnnouncement lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public DirectMessage getAnnouncerMessage() {
        return this.announcerMessage;
    }

    public void setAnnouncerMessage(DirectMessage directMessage) {
        this.announcerMessage = directMessage;
        this.announcerMessageId = directMessage != null ? directMessage.getId() : null;
    }

    public AdminAnnouncement announcerMessage(DirectMessage directMessage) {
        this.setAnnouncerMessage(directMessage);
        return this;
    }

    public AdminUserProfile getAdmin() {
        return this.admin;
    }

    public void setAdmin(AdminUserProfile adminUserProfile) {
        this.admin = adminUserProfile;
        this.adminId = adminUserProfile != null ? adminUserProfile.getId() : null;
    }

    public AdminAnnouncement admin(AdminUserProfile adminUserProfile) {
        this.setAdmin(adminUserProfile);
        return this;
    }

    public Long getAnnouncerMessageId() {
        return this.announcerMessageId;
    }

    public void setAnnouncerMessageId(Long directMessage) {
        this.announcerMessageId = directMessage;
    }

    public Long getAdminId() {
        return this.adminId;
    }

    public void setAdminId(Long adminUserProfile) {
        this.adminId = adminUserProfile;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminAnnouncement)) {
            return false;
        }
        return getId() != null && getId().equals(((AdminAnnouncement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminAnnouncement{" +
            "id=" + getId() +
            ", announcementType='" + getAnnouncementType() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
