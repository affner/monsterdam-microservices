package com.fanflip.admin.service.dto;

import com.fanflip.admin.domain.enumeration.EmailTemplateType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.AdminEmailConfigs} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdminEmailConfigsDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String title;

    @NotNull(message = "must not be null")
    private String subject;

    @NotNull(message = "must not be null")
    private String content;

    @NotNull(message = "must not be null")
    private EmailTemplateType mailTemplateType;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EmailTemplateType getMailTemplateType() {
        return mailTemplateType;
    }

    public void setMailTemplateType(EmailTemplateType mailTemplateType) {
        this.mailTemplateType = mailTemplateType;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminEmailConfigsDTO)) {
            return false;
        }

        AdminEmailConfigsDTO adminEmailConfigsDTO = (AdminEmailConfigsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, adminEmailConfigsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminEmailConfigsDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", subject='" + getSubject() + "'" +
            ", content='" + getContent() + "'" +
            ", mailTemplateType='" + getMailTemplateType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
