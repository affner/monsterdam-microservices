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
 * A ChatRoom.
 */
@Table("chat_room")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "chatroom")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("last_action")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lastAction;

    @Column("last_connection_date")
    private Instant lastConnectionDate;

    @Column("muted")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean muted;

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
    private Set<DirectMessage> sentMessages = new HashSet<>();

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
    private UserProfile user;

    @Column("user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChatRoom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastAction() {
        return this.lastAction;
    }

    public ChatRoom lastAction(String lastAction) {
        this.setLastAction(lastAction);
        return this;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public Instant getLastConnectionDate() {
        return this.lastConnectionDate;
    }

    public ChatRoom lastConnectionDate(Instant lastConnectionDate) {
        this.setLastConnectionDate(lastConnectionDate);
        return this;
    }

    public void setLastConnectionDate(Instant lastConnectionDate) {
        this.lastConnectionDate = lastConnectionDate;
    }

    public Boolean getMuted() {
        return this.muted;
    }

    public ChatRoom muted(Boolean muted) {
        this.setMuted(muted);
        return this;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ChatRoom createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ChatRoom lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ChatRoom createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public ChatRoom lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public ChatRoom isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<DirectMessage> getSentMessages() {
        return this.sentMessages;
    }

    public void setSentMessages(Set<DirectMessage> directMessages) {
        this.sentMessages = directMessages;
    }

    public ChatRoom sentMessages(Set<DirectMessage> directMessages) {
        this.setSentMessages(directMessages);
        return this;
    }

    public ChatRoom addSentMessages(DirectMessage directMessage) {
        this.sentMessages.add(directMessage);
        return this;
    }

    public ChatRoom removeSentMessages(DirectMessage directMessage) {
        this.sentMessages.remove(directMessage);
        return this;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
        this.userId = userProfile != null ? userProfile.getId() : null;
    }

    public ChatRoom user(UserProfile userProfile) {
        this.setUser(userProfile);
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userProfile) {
        this.userId = userProfile;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatRoom)) {
            return false;
        }
        return getId() != null && getId().equals(((ChatRoom) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatRoom{" +
            "id=" + getId() +
            ", lastAction='" + getLastAction() + "'" +
            ", lastConnectionDate='" + getLastConnectionDate() + "'" +
            ", muted='" + getMuted() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
