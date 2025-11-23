package com.fanflip.admin.service.dto;

import com.fanflip.admin.domain.enumeration.TicketStatus;
import com.fanflip.admin.domain.enumeration.TicketType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.AssistanceTicket} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssistanceTicketDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String subject;

    @NotNull(message = "must not be null")
    private String description;

    @NotNull(message = "must not be null")
    private TicketStatus status;

    @NotNull(message = "must not be null")
    private TicketType type;

    private Instant openedAt;

    private Instant closedAt;

    private String comments;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private ModerationActionDTO moderationAction;

    private AdminUserProfileDTO assignedAdmin;

    private UserProfileDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public Instant getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(Instant openedAt) {
        this.openedAt = openedAt;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public ModerationActionDTO getModerationAction() {
        return moderationAction;
    }

    public void setModerationAction(ModerationActionDTO moderationAction) {
        this.moderationAction = moderationAction;
    }

    public AdminUserProfileDTO getAssignedAdmin() {
        return assignedAdmin;
    }

    public void setAssignedAdmin(AdminUserProfileDTO assignedAdmin) {
        this.assignedAdmin = assignedAdmin;
    }

    public UserProfileDTO getUser() {
        return user;
    }

    public void setUser(UserProfileDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssistanceTicketDTO)) {
            return false;
        }

        AssistanceTicketDTO assistanceTicketDTO = (AssistanceTicketDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assistanceTicketDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssistanceTicketDTO{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", openedAt='" + getOpenedAt() + "'" +
            ", closedAt='" + getClosedAt() + "'" +
            ", comments='" + getComments() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", moderationAction=" + getModerationAction() +
            ", assignedAdmin=" + getAssignedAdmin() +
            ", user=" + getUser() +
            "}";
    }
}
