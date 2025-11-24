package com.monsterdam.admin.domain;

import com.monsterdam.admin.domain.enumeration.ReportCategory;
import com.monsterdam.admin.domain.enumeration.ReportStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UserReport.
 */
@Table("user_report")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "userreport")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("report_description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String reportDescription;

    @NotNull(message = "must not be null")
    @Column("status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ReportStatus status;

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

    @NotNull(message = "must not be null")
    @Column("report_category")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ReportCategory reportCategory;

    @NotNull(message = "must not be null")
    @Column("reporter_id")
    private Long reporterId;

    @NotNull(message = "must not be null")
    @Column("reported_id")
    private Long reportedId;

    @Column("multimedia_id")
    private Long multimediaId;

    @Column("message_id")
    private Long messageId;

    @Column("post_id")
    private Long postId;

    @Column("comment_id")
    private Long commentId;

    @Transient
    private AssistanceTicket ticket;

    @Column("ticket_id")
    private Long ticketId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportDescription() {
        return this.reportDescription;
    }

    public UserReport reportDescription(String reportDescription) {
        this.setReportDescription(reportDescription);
        return this;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public ReportStatus getStatus() {
        return this.status;
    }

    public UserReport status(ReportStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserReport createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserReport lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserReport createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserReport lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public UserReport isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public ReportCategory getReportCategory() {
        return this.reportCategory;
    }

    public UserReport reportCategory(ReportCategory reportCategory) {
        this.setReportCategory(reportCategory);
        return this;
    }

    public void setReportCategory(ReportCategory reportCategory) {
        this.reportCategory = reportCategory;
    }

    public Long getReporterId() {
        return this.reporterId;
    }

    public UserReport reporterId(Long reporterId) {
        this.setReporterId(reporterId);
        return this;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public Long getReportedId() {
        return this.reportedId;
    }

    public UserReport reportedId(Long reportedId) {
        this.setReportedId(reportedId);
        return this;
    }

    public void setReportedId(Long reportedId) {
        this.reportedId = reportedId;
    }

    public Long getMultimediaId() {
        return this.multimediaId;
    }

    public UserReport multimediaId(Long multimediaId) {
        this.setMultimediaId(multimediaId);
        return this;
    }

    public void setMultimediaId(Long multimediaId) {
        this.multimediaId = multimediaId;
    }

    public Long getMessageId() {
        return this.messageId;
    }

    public UserReport messageId(Long messageId) {
        this.setMessageId(messageId);
        return this;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getPostId() {
        return this.postId;
    }

    public UserReport postId(Long postId) {
        this.setPostId(postId);
        return this;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getCommentId() {
        return this.commentId;
    }

    public UserReport commentId(Long commentId) {
        this.setCommentId(commentId);
        return this;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public AssistanceTicket getTicket() {
        return this.ticket;
    }

    public void setTicket(AssistanceTicket assistanceTicket) {
        this.ticket = assistanceTicket;
        this.ticketId = assistanceTicket != null ? assistanceTicket.getId() : null;
    }

    public UserReport ticket(AssistanceTicket assistanceTicket) {
        this.setTicket(assistanceTicket);
        return this;
    }

    public Long getTicketId() {
        return this.ticketId;
    }

    public void setTicketId(Long assistanceTicket) {
        this.ticketId = assistanceTicket;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserReport)) {
            return false;
        }
        return getId() != null && getId().equals(((UserReport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserReport{" +
            "id=" + getId() +
            ", reportDescription='" + getReportDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", reportCategory='" + getReportCategory() + "'" +
            ", reporterId=" + getReporterId() +
            ", reportedId=" + getReportedId() +
            ", multimediaId=" + getMultimediaId() +
            ", messageId=" + getMessageId() +
            ", postId=" + getPostId() +
            ", commentId=" + getCommentId() +
            "}";
    }
}
