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
 * A DirectMessage.
 */
@Entity
@Table(name = "direct_message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "directmessage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DirectMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "message_content", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String messageContent;

    @Column(name = "read_date")
    private Instant readDate;

    @Column(name = "like_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer likeCount;

    @Column(name = "is_hidden")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isHidden;

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

    @Column(name = "replied_story_id")
    private Long repliedStoryId;

    @NotNull
    @Column(name = "sender_user_id", nullable = false)
    private Long senderUserId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "responseTo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "responses", "responseTo", "chatRooms" }, allowSetters = true)
    private Set<DirectMessage> responses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "responses", "responseTo", "chatRooms" }, allowSetters = true)
    private DirectMessage responseTo;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "sentMessages")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "sentMessages" }, allowSetters = true)
    private Set<ChatRoom> chatRooms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DirectMessage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageContent() {
        return this.messageContent;
    }

    public DirectMessage messageContent(String messageContent) {
        this.setMessageContent(messageContent);
        return this;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Instant getReadDate() {
        return this.readDate;
    }

    public DirectMessage readDate(Instant readDate) {
        this.setReadDate(readDate);
        return this;
    }

    public void setReadDate(Instant readDate) {
        this.readDate = readDate;
    }

    public Integer getLikeCount() {
        return this.likeCount;
    }

    public DirectMessage likeCount(Integer likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Boolean getIsHidden() {
        return this.isHidden;
    }

    public DirectMessage isHidden(Boolean isHidden) {
        this.setIsHidden(isHidden);
        return this;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public DirectMessage createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public DirectMessage lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public DirectMessage createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public DirectMessage lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public DirectMessage isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getRepliedStoryId() {
        return this.repliedStoryId;
    }

    public DirectMessage repliedStoryId(Long repliedStoryId) {
        this.setRepliedStoryId(repliedStoryId);
        return this;
    }

    public void setRepliedStoryId(Long repliedStoryId) {
        this.repliedStoryId = repliedStoryId;
    }

    public Long getSenderUserId() {
        return this.senderUserId;
    }

    public DirectMessage senderUserId(Long senderUserId) {
        this.setSenderUserId(senderUserId);
        return this;
    }

    public void setSenderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
    }

    public Set<DirectMessage> getResponses() {
        return this.responses;
    }

    public void setResponses(Set<DirectMessage> directMessages) {
        if (this.responses != null) {
            this.responses.forEach(i -> i.setResponseTo(null));
        }
        if (directMessages != null) {
            directMessages.forEach(i -> i.setResponseTo(this));
        }
        this.responses = directMessages;
    }

    public DirectMessage responses(Set<DirectMessage> directMessages) {
        this.setResponses(directMessages);
        return this;
    }

    public DirectMessage addResponses(DirectMessage directMessage) {
        this.responses.add(directMessage);
        directMessage.setResponseTo(this);
        return this;
    }

    public DirectMessage removeResponses(DirectMessage directMessage) {
        this.responses.remove(directMessage);
        directMessage.setResponseTo(null);
        return this;
    }

    public DirectMessage getResponseTo() {
        return this.responseTo;
    }

    public void setResponseTo(DirectMessage directMessage) {
        this.responseTo = directMessage;
    }

    public DirectMessage responseTo(DirectMessage directMessage) {
        this.setResponseTo(directMessage);
        return this;
    }

    public Set<ChatRoom> getChatRooms() {
        return this.chatRooms;
    }

    public void setChatRooms(Set<ChatRoom> chatRooms) {
        if (this.chatRooms != null) {
            this.chatRooms.forEach(i -> i.removeSentMessages(this));
        }
        if (chatRooms != null) {
            chatRooms.forEach(i -> i.addSentMessages(this));
        }
        this.chatRooms = chatRooms;
    }

    public DirectMessage chatRooms(Set<ChatRoom> chatRooms) {
        this.setChatRooms(chatRooms);
        return this;
    }

    public DirectMessage addChatRooms(ChatRoom chatRoom) {
        this.chatRooms.add(chatRoom);
        chatRoom.getSentMessages().add(this);
        return this;
    }

    public DirectMessage removeChatRooms(ChatRoom chatRoom) {
        this.chatRooms.remove(chatRoom);
        chatRoom.getSentMessages().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DirectMessage)) {
            return false;
        }
        return getId() != null && getId().equals(((DirectMessage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DirectMessage{" +
            "id=" + getId() +
            ", messageContent='" + getMessageContent() + "'" +
            ", readDate='" + getReadDate() + "'" +
            ", likeCount=" + getLikeCount() +
            ", isHidden='" + getIsHidden() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", repliedStoryId=" + getRepliedStoryId() +
            ", senderUserId=" + getSenderUserId() +
            "}";
    }
}
