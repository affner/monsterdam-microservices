package com.fanflip.admin.domain;

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
 * A PostFeed.
 */
@Table("post_feed")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "postfeed")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostFeed implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("post_content")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String postContent;

    @Column("is_hidden")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isHidden;

    @Column("pinned_post")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean pinnedPost;

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
    private PostPoll poll;

    @Transient
    private ContentPackage contentPackage;

    @Transient
    @JsonIgnoreProperties(
        value = { "ticket", "reporter", "reported", "story", "video", "photo", "audio", "liveStream", "message", "post", "postComment" },
        allowSetters = true
    )
    private Set<UserReport> reports = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "reports", "responses", "commentMentions", "post", "responseTo", "commenter" }, allowSetters = true)
    private Set<PostComment> comments = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "originPost", "originPostComment", "mentionedUser" }, allowSetters = true)
    private Set<UserMention> commentMentions = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "posts", "profiles" }, allowSetters = true)
    private Set<HashTag> hashTags = new HashSet<>();

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
    private UserProfile creator;

    @Transient
    @JsonIgnoreProperties(value = { "post", "message", "user" }, allowSetters = true)
    private Set<BookMark> bookMarks = new HashSet<>();

    @Column("poll_id")
    private Long pollId;

    @Column("content_package_id")
    private Long contentPackageId;

    @Column("creator_id")
    private Long creatorId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PostFeed id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostContent() {
        return this.postContent;
    }

    public PostFeed postContent(String postContent) {
        this.setPostContent(postContent);
        return this;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public Boolean getIsHidden() {
        return this.isHidden;
    }

    public PostFeed isHidden(Boolean isHidden) {
        this.setIsHidden(isHidden);
        return this;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public Boolean getPinnedPost() {
        return this.pinnedPost;
    }

    public PostFeed pinnedPost(Boolean pinnedPost) {
        this.setPinnedPost(pinnedPost);
        return this;
    }

    public void setPinnedPost(Boolean pinnedPost) {
        this.pinnedPost = pinnedPost;
    }

    public Long getLikeCount() {
        return this.likeCount;
    }

    public PostFeed likeCount(Long likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PostFeed createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public PostFeed lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PostFeed createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PostFeed lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public PostFeed isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public PostPoll getPoll() {
        return this.poll;
    }

    public void setPoll(PostPoll postPoll) {
        this.poll = postPoll;
        this.pollId = postPoll != null ? postPoll.getId() : null;
    }

    public PostFeed poll(PostPoll postPoll) {
        this.setPoll(postPoll);
        return this;
    }

    public ContentPackage getContentPackage() {
        return this.contentPackage;
    }

    public void setContentPackage(ContentPackage contentPackage) {
        this.contentPackage = contentPackage;
        this.contentPackageId = contentPackage != null ? contentPackage.getId() : null;
    }

    public PostFeed contentPackage(ContentPackage contentPackage) {
        this.setContentPackage(contentPackage);
        return this;
    }

    public Set<UserReport> getReports() {
        return this.reports;
    }

    public void setReports(Set<UserReport> userReports) {
        if (this.reports != null) {
            this.reports.forEach(i -> i.setPost(null));
        }
        if (userReports != null) {
            userReports.forEach(i -> i.setPost(this));
        }
        this.reports = userReports;
    }

    public PostFeed reports(Set<UserReport> userReports) {
        this.setReports(userReports);
        return this;
    }

    public PostFeed addReports(UserReport userReport) {
        this.reports.add(userReport);
        userReport.setPost(this);
        return this;
    }

    public PostFeed removeReports(UserReport userReport) {
        this.reports.remove(userReport);
        userReport.setPost(null);
        return this;
    }

    public Set<PostComment> getComments() {
        return this.comments;
    }

    public void setComments(Set<PostComment> postComments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setPost(null));
        }
        if (postComments != null) {
            postComments.forEach(i -> i.setPost(this));
        }
        this.comments = postComments;
    }

    public PostFeed comments(Set<PostComment> postComments) {
        this.setComments(postComments);
        return this;
    }

    public PostFeed addComments(PostComment postComment) {
        this.comments.add(postComment);
        postComment.setPost(this);
        return this;
    }

    public PostFeed removeComments(PostComment postComment) {
        this.comments.remove(postComment);
        postComment.setPost(null);
        return this;
    }

    public Set<UserMention> getCommentMentions() {
        return this.commentMentions;
    }

    public void setCommentMentions(Set<UserMention> userMentions) {
        if (this.commentMentions != null) {
            this.commentMentions.forEach(i -> i.setOriginPost(null));
        }
        if (userMentions != null) {
            userMentions.forEach(i -> i.setOriginPost(this));
        }
        this.commentMentions = userMentions;
    }

    public PostFeed commentMentions(Set<UserMention> userMentions) {
        this.setCommentMentions(userMentions);
        return this;
    }

    public PostFeed addCommentMentions(UserMention userMention) {
        this.commentMentions.add(userMention);
        userMention.setOriginPost(this);
        return this;
    }

    public PostFeed removeCommentMentions(UserMention userMention) {
        this.commentMentions.remove(userMention);
        userMention.setOriginPost(null);
        return this;
    }

    public Set<HashTag> getHashTags() {
        return this.hashTags;
    }

    public void setHashTags(Set<HashTag> hashTags) {
        this.hashTags = hashTags;
    }

    public PostFeed hashTags(Set<HashTag> hashTags) {
        this.setHashTags(hashTags);
        return this;
    }

    public PostFeed addHashTags(HashTag hashTag) {
        this.hashTags.add(hashTag);
        return this;
    }

    public PostFeed removeHashTags(HashTag hashTag) {
        this.hashTags.remove(hashTag);
        return this;
    }

    public UserProfile getCreator() {
        return this.creator;
    }

    public void setCreator(UserProfile userProfile) {
        this.creator = userProfile;
        this.creatorId = userProfile != null ? userProfile.getId() : null;
    }

    public PostFeed creator(UserProfile userProfile) {
        this.setCreator(userProfile);
        return this;
    }

    public Set<BookMark> getBookMarks() {
        return this.bookMarks;
    }

    public void setBookMarks(Set<BookMark> bookMarks) {
        if (this.bookMarks != null) {
            this.bookMarks.forEach(i -> i.setPost(null));
        }
        if (bookMarks != null) {
            bookMarks.forEach(i -> i.setPost(this));
        }
        this.bookMarks = bookMarks;
    }

    public PostFeed bookMarks(Set<BookMark> bookMarks) {
        this.setBookMarks(bookMarks);
        return this;
    }

    public PostFeed addBookMarks(BookMark bookMark) {
        this.bookMarks.add(bookMark);
        bookMark.setPost(this);
        return this;
    }

    public PostFeed removeBookMarks(BookMark bookMark) {
        this.bookMarks.remove(bookMark);
        bookMark.setPost(null);
        return this;
    }

    public Long getPollId() {
        return this.pollId;
    }

    public void setPollId(Long postPoll) {
        this.pollId = postPoll;
    }

    public Long getContentPackageId() {
        return this.contentPackageId;
    }

    public void setContentPackageId(Long contentPackage) {
        this.contentPackageId = contentPackage;
    }

    public Long getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Long userProfile) {
        this.creatorId = userProfile;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostFeed)) {
            return false;
        }
        return getId() != null && getId().equals(((PostFeed) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostFeed{" +
            "id=" + getId() +
            ", postContent='" + getPostContent() + "'" +
            ", isHidden='" + getIsHidden() + "'" +
            ", pinnedPost='" + getPinnedPost() + "'" +
            ", likeCount=" + getLikeCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
