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
 * A Notification.
 */
@Table("notification")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "notification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("read_date")
    private Instant readDate;

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

    @Column("post_comment_id")
    private Long postCommentId;

    @Column("post_feed_id")
    private Long postFeedId;

    @Column("direct_message_id")
    private Long directMessageId;

    @Column("user_mention_id")
    private Long userMentionId;

    @Column("like_mark_id")
    private Long likeMarkId;

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
    private UserProfile commentedUser;

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
    private UserProfile messagedUser;

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
    private UserProfile mentionerUserInPost;

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
    private UserProfile mentionerUserInComment;

    @Column("commented_user_id")
    private Long commentedUserId;

    @Column("messaged_user_id")
    private Long messagedUserId;

    @Column("mentioner_user_in_post_id")
    private Long mentionerUserInPostId;

    @Column("mentioner_user_in_comment_id")
    private Long mentionerUserInCommentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReadDate() {
        return this.readDate;
    }

    public Notification readDate(Instant readDate) {
        this.setReadDate(readDate);
        return this;
    }

    public void setReadDate(Instant readDate) {
        this.readDate = readDate;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Notification createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Notification lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Notification createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Notification lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public Notification isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getPostCommentId() {
        return this.postCommentId;
    }

    public Notification postCommentId(Long postCommentId) {
        this.setPostCommentId(postCommentId);
        return this;
    }

    public void setPostCommentId(Long postCommentId) {
        this.postCommentId = postCommentId;
    }

    public Long getPostFeedId() {
        return this.postFeedId;
    }

    public Notification postFeedId(Long postFeedId) {
        this.setPostFeedId(postFeedId);
        return this;
    }

    public void setPostFeedId(Long postFeedId) {
        this.postFeedId = postFeedId;
    }

    public Long getDirectMessageId() {
        return this.directMessageId;
    }

    public Notification directMessageId(Long directMessageId) {
        this.setDirectMessageId(directMessageId);
        return this;
    }

    public void setDirectMessageId(Long directMessageId) {
        this.directMessageId = directMessageId;
    }

    public Long getUserMentionId() {
        return this.userMentionId;
    }

    public Notification userMentionId(Long userMentionId) {
        this.setUserMentionId(userMentionId);
        return this;
    }

    public void setUserMentionId(Long userMentionId) {
        this.userMentionId = userMentionId;
    }

    public Long getLikeMarkId() {
        return this.likeMarkId;
    }

    public Notification likeMarkId(Long likeMarkId) {
        this.setLikeMarkId(likeMarkId);
        return this;
    }

    public void setLikeMarkId(Long likeMarkId) {
        this.likeMarkId = likeMarkId;
    }

    public UserProfile getCommentedUser() {
        return this.commentedUser;
    }

    public void setCommentedUser(UserProfile userProfile) {
        this.commentedUser = userProfile;
        this.commentedUserId = userProfile != null ? userProfile.getId() : null;
    }

    public Notification commentedUser(UserProfile userProfile) {
        this.setCommentedUser(userProfile);
        return this;
    }

    public UserProfile getMessagedUser() {
        return this.messagedUser;
    }

    public void setMessagedUser(UserProfile userProfile) {
        this.messagedUser = userProfile;
        this.messagedUserId = userProfile != null ? userProfile.getId() : null;
    }

    public Notification messagedUser(UserProfile userProfile) {
        this.setMessagedUser(userProfile);
        return this;
    }

    public UserProfile getMentionerUserInPost() {
        return this.mentionerUserInPost;
    }

    public void setMentionerUserInPost(UserProfile userProfile) {
        this.mentionerUserInPost = userProfile;
        this.mentionerUserInPostId = userProfile != null ? userProfile.getId() : null;
    }

    public Notification mentionerUserInPost(UserProfile userProfile) {
        this.setMentionerUserInPost(userProfile);
        return this;
    }

    public UserProfile getMentionerUserInComment() {
        return this.mentionerUserInComment;
    }

    public void setMentionerUserInComment(UserProfile userProfile) {
        this.mentionerUserInComment = userProfile;
        this.mentionerUserInCommentId = userProfile != null ? userProfile.getId() : null;
    }

    public Notification mentionerUserInComment(UserProfile userProfile) {
        this.setMentionerUserInComment(userProfile);
        return this;
    }

    public Long getCommentedUserId() {
        return this.commentedUserId;
    }

    public void setCommentedUserId(Long userProfile) {
        this.commentedUserId = userProfile;
    }

    public Long getMessagedUserId() {
        return this.messagedUserId;
    }

    public void setMessagedUserId(Long userProfile) {
        this.messagedUserId = userProfile;
    }

    public Long getMentionerUserInPostId() {
        return this.mentionerUserInPostId;
    }

    public void setMentionerUserInPostId(Long userProfile) {
        this.mentionerUserInPostId = userProfile;
    }

    public Long getMentionerUserInCommentId() {
        return this.mentionerUserInCommentId;
    }

    public void setMentionerUserInCommentId(Long userProfile) {
        this.mentionerUserInCommentId = userProfile;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", readDate='" + getReadDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", postCommentId=" + getPostCommentId() +
            ", postFeedId=" + getPostFeedId() +
            ", directMessageId=" + getDirectMessageId() +
            ", userMentionId=" + getUserMentionId() +
            ", likeMarkId=" + getLikeMarkId() +
            "}";
    }
}
