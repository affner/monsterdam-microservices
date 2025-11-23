package com.fanflip.interactions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PostFeed.
 */
@Entity
@Table(name = "post_feed")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "postfeed")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostFeed implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "post_content", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String postContent;

    @Column(name = "is_hidden")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isHidden;

    @Column(name = "pinned_post")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean pinnedPost;

    @Column(name = "like_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer likeCount;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    @Column(name = "last_modified_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lastModifiedBy;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isDeleted;

    @NotNull
    @Column(name = "creator_user_id", nullable = false)
    private Long creatorUserId;

    @JsonIgnoreProperties(value = { "options", "post" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private PostPoll poll;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "responses", "commentMentions", "post", "responseTo" }, allowSetters = true)
    private Set<PostComment> comments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "originPost")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "originPost", "originPostComment" }, allowSetters = true)
    private Set<UserMention> commentMentions = new HashSet<>();

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

    public Integer getLikeCount() {
        return this.likeCount;
    }

    public PostFeed likeCount(Integer likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    public void setLikeCount(Integer likeCount) {
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

    public Long getCreatorUserId() {
        return this.creatorUserId;
    }

    public PostFeed creatorUserId(Long creatorUserId) {
        this.setCreatorUserId(creatorUserId);
        return this;
    }

    public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public PostPoll getPoll() {
        return this.poll;
    }

    public void setPoll(PostPoll postPoll) {
        this.poll = postPoll;
    }

    public PostFeed poll(PostPoll postPoll) {
        this.setPoll(postPoll);
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
            ", creatorUserId=" + getCreatorUserId() +
            "}";
    }
}
