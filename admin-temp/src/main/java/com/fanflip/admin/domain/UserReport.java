package com.monsterdam.admin.domain;

import com.monsterdam.admin.domain.enumeration.ReportCategory;
import com.monsterdam.admin.domain.enumeration.ReportStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Transient
    private AssistanceTicket ticket;

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
    private UserProfile reporter;

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
    private UserProfile reported;

    @Transient
    @JsonIgnoreProperties(value = { "reports", "messages", "creator" }, allowSetters = true)
    private VideoStory story;

    @Transient
    @JsonIgnoreProperties(value = { "reports", "belongPackage" }, allowSetters = true)
    private SingleVideo video;

    @Transient
    @JsonIgnoreProperties(value = { "reports", "belongPackage" }, allowSetters = true)
    private SinglePhoto photo;

    @Transient
    @JsonIgnoreProperties(value = { "reports", "contentPackage" }, allowSetters = true)
    private SingleAudio audio;

    @Transient
    @JsonIgnoreProperties(value = { "reports" }, allowSetters = true)
    private SingleLiveStream liveStream;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "contentPackage",
            "reports",
            "responses",
            "responseTo",
            "repliedStory",
            "user",
            "bookMarks",
            "chatRooms",
            "adminAnnouncement",
            "purchasedTip",
        },
        allowSetters = true
    )
    private DirectMessage message;

    @Transient
    @JsonIgnoreProperties(
        value = { "poll", "contentPackage", "reports", "comments", "commentMentions", "hashTags", "creator", "bookMarks" },
        allowSetters = true
    )
    private PostFeed post;

    @Transient
    @JsonIgnoreProperties(value = { "reports", "responses", "commentMentions", "post", "responseTo", "commenter" }, allowSetters = true)
    private PostComment postComment;

    @Column("ticket_id")
    private Long ticketId;

    @Column("reporter_id")
    private Long reporterId;

    @Column("reported_id")
    private Long reportedId;

    @Column("story_id")
    private Long storyId;

    @Column("video_id")
    private Long videoId;

    @Column("photo_id")
    private Long photoId;

    @Column("audio_id")
    private Long audioId;

    @Column("live_stream_id")
    private Long liveStreamId;

    @Column("message_id")
    private Long messageId;

    @Column("post_id")
    private Long postId;

    @Column("post_comment_id")
    private Long postCommentId;

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

    public UserProfile getReporter() {
        return this.reporter;
    }

    public void setReporter(UserProfile userProfile) {
        this.reporter = userProfile;
        this.reporterId = userProfile != null ? userProfile.getId() : null;
    }

    public UserReport reporter(UserProfile userProfile) {
        this.setReporter(userProfile);
        return this;
    }

    public UserProfile getReported() {
        return this.reported;
    }

    public void setReported(UserProfile userProfile) {
        this.reported = userProfile;
        this.reportedId = userProfile != null ? userProfile.getId() : null;
    }

    public UserReport reported(UserProfile userProfile) {
        this.setReported(userProfile);
        return this;
    }

    public VideoStory getStory() {
        return this.story;
    }

    public void setStory(VideoStory videoStory) {
        this.story = videoStory;
        this.storyId = videoStory != null ? videoStory.getId() : null;
    }

    public UserReport story(VideoStory videoStory) {
        this.setStory(videoStory);
        return this;
    }

    public SingleVideo getVideo() {
        return this.video;
    }

    public void setVideo(SingleVideo singleVideo) {
        this.video = singleVideo;
        this.videoId = singleVideo != null ? singleVideo.getId() : null;
    }

    public UserReport video(SingleVideo singleVideo) {
        this.setVideo(singleVideo);
        return this;
    }

    public SinglePhoto getPhoto() {
        return this.photo;
    }

    public void setPhoto(SinglePhoto singlePhoto) {
        this.photo = singlePhoto;
        this.photoId = singlePhoto != null ? singlePhoto.getId() : null;
    }

    public UserReport photo(SinglePhoto singlePhoto) {
        this.setPhoto(singlePhoto);
        return this;
    }

    public SingleAudio getAudio() {
        return this.audio;
    }

    public void setAudio(SingleAudio singleAudio) {
        this.audio = singleAudio;
        this.audioId = singleAudio != null ? singleAudio.getId() : null;
    }

    public UserReport audio(SingleAudio singleAudio) {
        this.setAudio(singleAudio);
        return this;
    }

    public SingleLiveStream getLiveStream() {
        return this.liveStream;
    }

    public void setLiveStream(SingleLiveStream singleLiveStream) {
        this.liveStream = singleLiveStream;
        this.liveStreamId = singleLiveStream != null ? singleLiveStream.getId() : null;
    }

    public UserReport liveStream(SingleLiveStream singleLiveStream) {
        this.setLiveStream(singleLiveStream);
        return this;
    }

    public DirectMessage getMessage() {
        return this.message;
    }

    public void setMessage(DirectMessage directMessage) {
        this.message = directMessage;
        this.messageId = directMessage != null ? directMessage.getId() : null;
    }

    public UserReport message(DirectMessage directMessage) {
        this.setMessage(directMessage);
        return this;
    }

    public PostFeed getPost() {
        return this.post;
    }

    public void setPost(PostFeed postFeed) {
        this.post = postFeed;
        this.postId = postFeed != null ? postFeed.getId() : null;
    }

    public UserReport post(PostFeed postFeed) {
        this.setPost(postFeed);
        return this;
    }

    public PostComment getPostComment() {
        return this.postComment;
    }

    public void setPostComment(PostComment postComment) {
        this.postComment = postComment;
        this.postCommentId = postComment != null ? postComment.getId() : null;
    }

    public UserReport postComment(PostComment postComment) {
        this.setPostComment(postComment);
        return this;
    }

    public Long getTicketId() {
        return this.ticketId;
    }

    public void setTicketId(Long assistanceTicket) {
        this.ticketId = assistanceTicket;
    }

    public Long getReporterId() {
        return this.reporterId;
    }

    public void setReporterId(Long userProfile) {
        this.reporterId = userProfile;
    }

    public Long getReportedId() {
        return this.reportedId;
    }

    public void setReportedId(Long userProfile) {
        this.reportedId = userProfile;
    }

    public Long getStoryId() {
        return this.storyId;
    }

    public void setStoryId(Long videoStory) {
        this.storyId = videoStory;
    }

    public Long getVideoId() {
        return this.videoId;
    }

    public void setVideoId(Long singleVideo) {
        this.videoId = singleVideo;
    }

    public Long getPhotoId() {
        return this.photoId;
    }

    public void setPhotoId(Long singlePhoto) {
        this.photoId = singlePhoto;
    }

    public Long getAudioId() {
        return this.audioId;
    }

    public void setAudioId(Long singleAudio) {
        this.audioId = singleAudio;
    }

    public Long getLiveStreamId() {
        return this.liveStreamId;
    }

    public void setLiveStreamId(Long singleLiveStream) {
        this.liveStreamId = singleLiveStream;
    }

    public Long getMessageId() {
        return this.messageId;
    }

    public void setMessageId(Long directMessage) {
        this.messageId = directMessage;
    }

    public Long getPostId() {
        return this.postId;
    }

    public void setPostId(Long postFeed) {
        this.postId = postFeed;
    }

    public Long getPostCommentId() {
        return this.postCommentId;
    }

    public void setPostCommentId(Long postComment) {
        this.postCommentId = postComment;
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
            "}";
    }
}
