package com.monsterdam.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PostComment.
 */
@Table("post_comment")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "postcomment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("comment_content")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String commentContent;

    @Column("like_count")
    private Long likeCount;

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
    @JsonIgnoreProperties(
        value = { "ticket", "reporter", "reported", "story", "video", "photo", "audio", "liveStream", "message", "post", "postComment" },
        allowSetters = true
    )
    private Set<UserReport> reports = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "reports", "responses", "commentMentions", "post", "responseTo", "commenter" }, allowSetters = true)
    private Set<PostComment> responses = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "originPost", "originPostComment", "mentionedUser" }, allowSetters = true)
    private Set<UserMention> commentMentions = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = { "poll", "contentPackage", "reports", "comments", "commentMentions", "hashTags", "creator", "bookMarks" },
        allowSetters = true
    )
    private PostFeed post;

    @Transient
    @JsonIgnoreProperties(value = { "reports", "responses", "commentMentions", "post", "responseTo", "commenter" }, allowSetters = true)
    private PostComment responseTo;

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
    private UserProfile commenter;

    @Column("post_id")
    private Long postId;

    @Column("response_to_id")
    private Long responseToId;

    @Column("commenter_id")
    private Long commenterId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PostComment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentContent() {
        return this.commentContent;
    }

    public PostComment commentContent(String commentContent) {
        this.setCommentContent(commentContent);
        return this;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Long getLikeCount() {
        return this.likeCount;
    }

    public PostComment likeCount(Long likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PostComment createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public PostComment lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PostComment createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PostComment lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public PostComment isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<UserReport> getReports() {
        return this.reports;
    }

    public void setReports(Set<UserReport> userReports) {
        if (this.reports != null) {
            this.reports.forEach(i -> i.setPostComment(null));
        }
        if (userReports != null) {
            userReports.forEach(i -> i.setPostComment(this));
        }
        this.reports = userReports;
    }

    public PostComment reports(Set<UserReport> userReports) {
        this.setReports(userReports);
        return this;
    }

    public PostComment addReports(UserReport userReport) {
        this.reports.add(userReport);
        userReport.setPostComment(this);
        return this;
    }

    public PostComment removeReports(UserReport userReport) {
        this.reports.remove(userReport);
        userReport.setPostComment(null);
        return this;
    }

    public Set<PostComment> getResponses() {
        return this.responses;
    }

    public void setResponses(Set<PostComment> postComments) {
        if (this.responses != null) {
            this.responses.forEach(i -> i.setResponseTo(null));
        }
        if (postComments != null) {
            postComments.forEach(i -> i.setResponseTo(this));
        }
        this.responses = postComments;
    }

    public PostComment responses(Set<PostComment> postComments) {
        this.setResponses(postComments);
        return this;
    }

    public PostComment addResponses(PostComment postComment) {
        this.responses.add(postComment);
        postComment.setResponseTo(this);
        return this;
    }

    public PostComment removeResponses(PostComment postComment) {
        this.responses.remove(postComment);
        postComment.setResponseTo(null);
        return this;
    }

    public Set<UserMention> getCommentMentions() {
        return this.commentMentions;
    }

    public void setCommentMentions(Set<UserMention> userMentions) {
        if (this.commentMentions != null) {
            this.commentMentions.forEach(i -> i.setOriginPostComment(null));
        }
        if (userMentions != null) {
            userMentions.forEach(i -> i.setOriginPostComment(this));
        }
        this.commentMentions = userMentions;
    }

    public PostComment commentMentions(Set<UserMention> userMentions) {
        this.setCommentMentions(userMentions);
        return this;
    }

    public PostComment addCommentMentions(UserMention userMention) {
        this.commentMentions.add(userMention);
        userMention.setOriginPostComment(this);
        return this;
    }

    public PostComment removeCommentMentions(UserMention userMention) {
        this.commentMentions.remove(userMention);
        userMention.setOriginPostComment(null);
        return this;
    }

    public PostFeed getPost() {
        return this.post;
    }

    public void setPost(PostFeed postFeed) {
        this.post = postFeed;
        this.postId = postFeed != null ? postFeed.getId() : null;
    }

    public PostComment post(PostFeed postFeed) {
        this.setPost(postFeed);
        return this;
    }

    public PostComment getResponseTo() {
        return this.responseTo;
    }

    public void setResponseTo(PostComment postComment) {
        this.responseTo = postComment;
        this.responseToId = postComment != null ? postComment.getId() : null;
    }

    public PostComment responseTo(PostComment postComment) {
        this.setResponseTo(postComment);
        return this;
    }

    public UserProfile getCommenter() {
        return this.commenter;
    }

    public void setCommenter(UserProfile userProfile) {
        this.commenter = userProfile;
        this.commenterId = userProfile != null ? userProfile.getId() : null;
    }

    public PostComment commenter(UserProfile userProfile) {
        this.setCommenter(userProfile);
        return this;
    }

    public Long getPostId() {
        return this.postId;
    }

    public void setPostId(Long postFeed) {
        this.postId = postFeed;
    }

    public Long getResponseToId() {
        return this.responseToId;
    }

    public void setResponseToId(Long postComment) {
        this.responseToId = postComment;
    }

    public Long getCommenterId() {
        return this.commenterId;
    }

    public void setCommenterId(Long userProfile) {
        this.commenterId = userProfile;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostComment)) {
            return false;
        }
        return getId() != null && getId().equals(((PostComment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostComment{" +
            "id=" + getId() +
            ", commentContent='" + getCommentContent() + "'" +
            ", likeCount=" + getLikeCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
