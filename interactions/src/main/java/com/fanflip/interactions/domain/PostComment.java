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
 * A PostComment.
 */
@Entity
@Table(name = "post_comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "postcomment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "comment_content", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String commentContent;

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
    @Column(name = "commenter_user_id", nullable = false)
    private Long commenterUserId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "responseTo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "responses", "commentMentions", "post", "responseTo" }, allowSetters = true)
    private Set<PostComment> responses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "originPostComment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "originPost", "originPostComment" }, allowSetters = true)
    private Set<UserMention> commentMentions = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "poll", "comments", "commentMentions" }, allowSetters = true)
    private PostFeed post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "responses", "commentMentions", "post", "responseTo" }, allowSetters = true)
    private PostComment responseTo;

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

    public Integer getLikeCount() {
        return this.likeCount;
    }

    public PostComment likeCount(Integer likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    public void setLikeCount(Integer likeCount) {
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

    public Long getCommenterUserId() {
        return this.commenterUserId;
    }

    public PostComment commenterUserId(Long commenterUserId) {
        this.setCommenterUserId(commenterUserId);
        return this;
    }

    public void setCommenterUserId(Long commenterUserId) {
        this.commenterUserId = commenterUserId;
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
    }

    public PostComment responseTo(PostComment postComment) {
        this.setResponseTo(postComment);
        return this;
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
            ", commenterUserId=" + getCommenterUserId() +
            "}";
    }
}
