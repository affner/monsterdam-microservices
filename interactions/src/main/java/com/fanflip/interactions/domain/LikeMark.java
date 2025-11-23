package com.fanflip.interactions.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LikeMark.
 */
@Entity
@Table(name = "like_mark")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LikeMark implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "emoji_type_id", nullable = false)
    private Long emojiTypeId;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "multimedia_id")
    private Long multimediaId;

    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "liker_user_id")
    private Long likerUserId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LikeMark id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmojiTypeId() {
        return this.emojiTypeId;
    }

    public LikeMark emojiTypeId(Long emojiTypeId) {
        this.setEmojiTypeId(emojiTypeId);
        return this;
    }

    public void setEmojiTypeId(Long emojiTypeId) {
        this.emojiTypeId = emojiTypeId;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public LikeMark createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public LikeMark lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public LikeMark createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public LikeMark lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public LikeMark isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getMultimediaId() {
        return this.multimediaId;
    }

    public LikeMark multimediaId(Long multimediaId) {
        this.setMultimediaId(multimediaId);
        return this;
    }

    public void setMultimediaId(Long multimediaId) {
        this.multimediaId = multimediaId;
    }

    public Long getMessageId() {
        return this.messageId;
    }

    public LikeMark messageId(Long messageId) {
        this.setMessageId(messageId);
        return this;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getPostId() {
        return this.postId;
    }

    public LikeMark postId(Long postId) {
        this.setPostId(postId);
        return this;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getCommentId() {
        return this.commentId;
    }

    public LikeMark commentId(Long commentId) {
        this.setCommentId(commentId);
        return this;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getLikerUserId() {
        return this.likerUserId;
    }

    public LikeMark likerUserId(Long likerUserId) {
        this.setLikerUserId(likerUserId);
        return this;
    }

    public void setLikerUserId(Long likerUserId) {
        this.likerUserId = likerUserId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LikeMark)) {
            return false;
        }
        return getId() != null && getId().equals(((LikeMark) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikeMark{" +
            "id=" + getId() +
            ", emojiTypeId=" + getEmojiTypeId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", multimediaId=" + getMultimediaId() +
            ", messageId=" + getMessageId() +
            ", postId=" + getPostId() +
            ", commentId=" + getCommentId() +
            ", likerUserId=" + getLikerUserId() +
            "}";
    }
}
