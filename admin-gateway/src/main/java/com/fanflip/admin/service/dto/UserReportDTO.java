package com.fanflip.admin.service.dto;

import com.fanflip.admin.domain.enumeration.ReportCategory;
import com.fanflip.admin.domain.enumeration.ReportStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.UserReport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserReportDTO implements Serializable {

    private Long id;

    private String reportDescription;

    @NotNull(message = "must not be null")
    private ReportStatus status;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    @NotNull(message = "must not be null")
    private ReportCategory reportCategory;

    @NotNull(message = "must not be null")
    private Long reporterId;

    @NotNull(message = "must not be null")
    private Long reportedId;

    private Long multimediaId;

    private Long messageId;

    private Long postId;

    private Long commentId;

    private AssistanceTicketDTO ticket;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
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

    public ReportCategory getReportCategory() {
        return reportCategory;
    }

    public void setReportCategory(ReportCategory reportCategory) {
        this.reportCategory = reportCategory;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public Long getReportedId() {
        return reportedId;
    }

    public void setReportedId(Long reportedId) {
        this.reportedId = reportedId;
    }

    public Long getMultimediaId() {
        return multimediaId;
    }

    public void setMultimediaId(Long multimediaId) {
        this.multimediaId = multimediaId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public AssistanceTicketDTO getTicket() {
        return ticket;
    }

    public void setTicket(AssistanceTicketDTO ticket) {
        this.ticket = ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserReportDTO)) {
            return false;
        }

        UserReportDTO userReportDTO = (UserReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userReportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserReportDTO{" +
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
            ", ticket=" + getTicket() +
            "}";
    }
}
