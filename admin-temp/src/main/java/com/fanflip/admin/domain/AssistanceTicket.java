package com.fanflip.admin.domain;

import com.fanflip.admin.domain.enumeration.TicketStatus;
import com.fanflip.admin.domain.enumeration.TicketType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AssistanceTicket.
 */
@Table("assistance_ticket")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assistanceticket")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssistanceTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("subject")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String subject;

    @NotNull(message = "must not be null")
    @Column("description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @NotNull(message = "must not be null")
    @Column("status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TicketStatus status;

    @NotNull(message = "must not be null")
    @Column("type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TicketType type;

    @Column("opened_at")
    private Instant openedAt;

    @Column("closed_at")
    private Instant closedAt;

    @Column("comments")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String comments;

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
    private ModerationAction moderationAction;

    @Transient
    private UserReport report;

    @Transient
    private IdentityDocumentReview documentsReview;

    @Transient
    @JsonIgnoreProperties(value = { "assignedTickets", "announcements" }, allowSetters = true)
    private AdminUserProfile assignedAdmin;

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

    @Column("moderation_action_id")
    private Long moderationActionId;

    @Column("assigned_admin_id")
    private Long assignedAdminId;

    @Column("user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssistanceTicket id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return this.subject;
    }

    public AssistanceTicket subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return this.description;
    }

    public AssistanceTicket description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return this.status;
    }

    public AssistanceTicket status(TicketStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketType getType() {
        return this.type;
    }

    public AssistanceTicket type(TicketType type) {
        this.setType(type);
        return this;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public Instant getOpenedAt() {
        return this.openedAt;
    }

    public AssistanceTicket openedAt(Instant openedAt) {
        this.setOpenedAt(openedAt);
        return this;
    }

    public void setOpenedAt(Instant openedAt) {
        this.openedAt = openedAt;
    }

    public Instant getClosedAt() {
        return this.closedAt;
    }

    public AssistanceTicket closedAt(Instant closedAt) {
        this.setClosedAt(closedAt);
        return this;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public String getComments() {
        return this.comments;
    }

    public AssistanceTicket comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public AssistanceTicket createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public AssistanceTicket lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public AssistanceTicket createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public AssistanceTicket lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ModerationAction getModerationAction() {
        return this.moderationAction;
    }

    public void setModerationAction(ModerationAction moderationAction) {
        this.moderationAction = moderationAction;
        this.moderationActionId = moderationAction != null ? moderationAction.getId() : null;
    }

    public AssistanceTicket moderationAction(ModerationAction moderationAction) {
        this.setModerationAction(moderationAction);
        return this;
    }

    public UserReport getReport() {
        return this.report;
    }

    public void setReport(UserReport userReport) {
        if (this.report != null) {
            this.report.setTicket(null);
        }
        if (userReport != null) {
            userReport.setTicket(this);
        }
        this.report = userReport;
    }

    public AssistanceTicket report(UserReport userReport) {
        this.setReport(userReport);
        return this;
    }

    public IdentityDocumentReview getDocumentsReview() {
        return this.documentsReview;
    }

    public void setDocumentsReview(IdentityDocumentReview identityDocumentReview) {
        if (this.documentsReview != null) {
            this.documentsReview.setTicket(null);
        }
        if (identityDocumentReview != null) {
            identityDocumentReview.setTicket(this);
        }
        this.documentsReview = identityDocumentReview;
    }

    public AssistanceTicket documentsReview(IdentityDocumentReview identityDocumentReview) {
        this.setDocumentsReview(identityDocumentReview);
        return this;
    }

    public AdminUserProfile getAssignedAdmin() {
        return this.assignedAdmin;
    }

    public void setAssignedAdmin(AdminUserProfile adminUserProfile) {
        this.assignedAdmin = adminUserProfile;
        this.assignedAdminId = adminUserProfile != null ? adminUserProfile.getId() : null;
    }

    public AssistanceTicket assignedAdmin(AdminUserProfile adminUserProfile) {
        this.setAssignedAdmin(adminUserProfile);
        return this;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
        this.userId = userProfile != null ? userProfile.getId() : null;
    }

    public AssistanceTicket user(UserProfile userProfile) {
        this.setUser(userProfile);
        return this;
    }

    public Long getModerationActionId() {
        return this.moderationActionId;
    }

    public void setModerationActionId(Long moderationAction) {
        this.moderationActionId = moderationAction;
    }

    public Long getAssignedAdminId() {
        return this.assignedAdminId;
    }

    public void setAssignedAdminId(Long adminUserProfile) {
        this.assignedAdminId = adminUserProfile;
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
        if (!(o instanceof AssistanceTicket)) {
            return false;
        }
        return getId() != null && getId().equals(((AssistanceTicket) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssistanceTicket{" +
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
            "}";
    }
}
