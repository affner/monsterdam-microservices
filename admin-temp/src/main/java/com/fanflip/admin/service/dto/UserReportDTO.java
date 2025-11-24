package com.monsterdam.admin.service.dto;

import com.monsterdam.admin.domain.enumeration.ReportCategory;
import com.monsterdam.admin.domain.enumeration.ReportStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.UserReport} entity.
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

    private AssistanceTicketDTO ticket;

    private UserProfileDTO reporter;

    private UserProfileDTO reported;

    private VideoStoryDTO story;

    private SingleVideoDTO video;

    private SinglePhotoDTO photo;

    private SingleAudioDTO audio;

    private SingleLiveStreamDTO liveStream;

    private DirectMessageDTO message;

    private PostFeedDTO post;

    private PostCommentDTO postComment;

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

    public AssistanceTicketDTO getTicket() {
        return ticket;
    }

    public void setTicket(AssistanceTicketDTO ticket) {
        this.ticket = ticket;
    }

    public UserProfileDTO getReporter() {
        return reporter;
    }

    public void setReporter(UserProfileDTO reporter) {
        this.reporter = reporter;
    }

    public UserProfileDTO getReported() {
        return reported;
    }

    public void setReported(UserProfileDTO reported) {
        this.reported = reported;
    }

    public VideoStoryDTO getStory() {
        return story;
    }

    public void setStory(VideoStoryDTO story) {
        this.story = story;
    }

    public SingleVideoDTO getVideo() {
        return video;
    }

    public void setVideo(SingleVideoDTO video) {
        this.video = video;
    }

    public SinglePhotoDTO getPhoto() {
        return photo;
    }

    public void setPhoto(SinglePhotoDTO photo) {
        this.photo = photo;
    }

    public SingleAudioDTO getAudio() {
        return audio;
    }

    public void setAudio(SingleAudioDTO audio) {
        this.audio = audio;
    }

    public SingleLiveStreamDTO getLiveStream() {
        return liveStream;
    }

    public void setLiveStream(SingleLiveStreamDTO liveStream) {
        this.liveStream = liveStream;
    }

    public DirectMessageDTO getMessage() {
        return message;
    }

    public void setMessage(DirectMessageDTO message) {
        this.message = message;
    }

    public PostFeedDTO getPost() {
        return post;
    }

    public void setPost(PostFeedDTO post) {
        this.post = post;
    }

    public PostCommentDTO getPostComment() {
        return postComment;
    }

    public void setPostComment(PostCommentDTO postComment) {
        this.postComment = postComment;
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
            ", ticket=" + getTicket() +
            ", reporter=" + getReporter() +
            ", reported=" + getReported() +
            ", story=" + getStory() +
            ", video=" + getVideo() +
            ", photo=" + getPhoto() +
            ", audio=" + getAudio() +
            ", liveStream=" + getLiveStream() +
            ", message=" + getMessage() +
            ", post=" + getPost() +
            ", postComment=" + getPostComment() +
            "}";
    }
}
