package com.monsterdam.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UserMention.
 */
@Table("user_mention")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "usermention")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserMention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

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
        value = { "poll", "contentPackage", "reports", "comments", "commentMentions", "hashTags", "creator", "bookMarks" },
        allowSetters = true
    )
    private PostFeed originPost;

    @Transient
    @JsonIgnoreProperties(value = { "reports", "responses", "commentMentions", "post", "responseTo", "commenter" }, allowSetters = true)
    private PostComment originPostComment;

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
    private UserProfile mentionedUser;

    @Column("origin_post_id")
    private Long originPostId;

    @Column("origin_post_comment_id")
    private Long originPostCommentId;

    @Column("mentioned_user_id")
    private Long mentionedUserId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserMention id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserMention createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserMention lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserMention createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserMention lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public UserMention isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public PostFeed getOriginPost() {
        return this.originPost;
    }

    public void setOriginPost(PostFeed postFeed) {
        this.originPost = postFeed;
        this.originPostId = postFeed != null ? postFeed.getId() : null;
    }

    public UserMention originPost(PostFeed postFeed) {
        this.setOriginPost(postFeed);
        return this;
    }

    public PostComment getOriginPostComment() {
        return this.originPostComment;
    }

    public void setOriginPostComment(PostComment postComment) {
        this.originPostComment = postComment;
        this.originPostCommentId = postComment != null ? postComment.getId() : null;
    }

    public UserMention originPostComment(PostComment postComment) {
        this.setOriginPostComment(postComment);
        return this;
    }

    public UserProfile getMentionedUser() {
        return this.mentionedUser;
    }

    public void setMentionedUser(UserProfile userProfile) {
        this.mentionedUser = userProfile;
        this.mentionedUserId = userProfile != null ? userProfile.getId() : null;
    }

    public UserMention mentionedUser(UserProfile userProfile) {
        this.setMentionedUser(userProfile);
        return this;
    }

    public Long getOriginPostId() {
        return this.originPostId;
    }

    public void setOriginPostId(Long postFeed) {
        this.originPostId = postFeed;
    }

    public Long getOriginPostCommentId() {
        return this.originPostCommentId;
    }

    public void setOriginPostCommentId(Long postComment) {
        this.originPostCommentId = postComment;
    }

    public Long getMentionedUserId() {
        return this.mentionedUserId;
    }

    public void setMentionedUserId(Long userProfile) {
        this.mentionedUserId = userProfile;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserMention)) {
            return false;
        }
        return getId() != null && getId().equals(((UserMention) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserMention{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
