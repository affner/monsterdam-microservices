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
 * A DirectMessage.
 */
@Table("direct_message")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "directmessage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DirectMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("message_content")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String messageContent;

    @Column("read_date")
    private Instant readDate;

    @Column("like_count")
    private Long likeCount;

    @Column("is_hidden")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isHidden;

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
    private ContentPackage contentPackage;

    @Transient
    @JsonIgnoreProperties(
        value = { "ticket", "reporter", "reported", "story", "video", "photo", "audio", "liveStream", "message", "post", "postComment" },
        allowSetters = true
    )
    private Set<UserReport> reports = new HashSet<>();

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
    private Set<DirectMessage> responses = new HashSet<>();

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
    private DirectMessage responseTo;

    @Transient
    @JsonIgnoreProperties(value = { "reports", "messages", "creator" }, allowSetters = true)
    private VideoStory repliedStory;

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

    @Transient
    @JsonIgnoreProperties(value = { "post", "message", "user" }, allowSetters = true)
    private Set<BookMark> bookMarks = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "sentMessages", "user" }, allowSetters = true)
    private Set<ChatRoom> chatRooms = new HashSet<>();

    @Transient
    private AdminAnnouncement adminAnnouncement;

    @Transient
    private PurchasedTip purchasedTip;

    @Column("content_package_id")
    private Long contentPackageId;

    @Column("response_to_id")
    private Long responseToId;

    @Column("replied_story_id")
    private Long repliedStoryId;

    @Column("user_id")
    private Long userId;

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

    public Long getLikeCount() {
        return this.likeCount;
    }

    public DirectMessage likeCount(Long likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    public void setLikeCount(Long likeCount) {
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

    public ContentPackage getContentPackage() {
        return this.contentPackage;
    }

    public void setContentPackage(ContentPackage contentPackage) {
        this.contentPackage = contentPackage;
        this.contentPackageId = contentPackage != null ? contentPackage.getId() : null;
    }

    public DirectMessage contentPackage(ContentPackage contentPackage) {
        this.setContentPackage(contentPackage);
        return this;
    }

    public Set<UserReport> getReports() {
        return this.reports;
    }

    public void setReports(Set<UserReport> userReports) {
        if (this.reports != null) {
            this.reports.forEach(i -> i.setMessage(null));
        }
        if (userReports != null) {
            userReports.forEach(i -> i.setMessage(this));
        }
        this.reports = userReports;
    }

    public DirectMessage reports(Set<UserReport> userReports) {
        this.setReports(userReports);
        return this;
    }

    public DirectMessage addReports(UserReport userReport) {
        this.reports.add(userReport);
        userReport.setMessage(this);
        return this;
    }

    public DirectMessage removeReports(UserReport userReport) {
        this.reports.remove(userReport);
        userReport.setMessage(null);
        return this;
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
        this.responseToId = directMessage != null ? directMessage.getId() : null;
    }

    public DirectMessage responseTo(DirectMessage directMessage) {
        this.setResponseTo(directMessage);
        return this;
    }

    public VideoStory getRepliedStory() {
        return this.repliedStory;
    }

    public void setRepliedStory(VideoStory videoStory) {
        this.repliedStory = videoStory;
        this.repliedStoryId = videoStory != null ? videoStory.getId() : null;
    }

    public DirectMessage repliedStory(VideoStory videoStory) {
        this.setRepliedStory(videoStory);
        return this;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
        this.userId = userProfile != null ? userProfile.getId() : null;
    }

    public DirectMessage user(UserProfile userProfile) {
        this.setUser(userProfile);
        return this;
    }

    public Set<BookMark> getBookMarks() {
        return this.bookMarks;
    }

    public void setBookMarks(Set<BookMark> bookMarks) {
        if (this.bookMarks != null) {
            this.bookMarks.forEach(i -> i.setMessage(null));
        }
        if (bookMarks != null) {
            bookMarks.forEach(i -> i.setMessage(this));
        }
        this.bookMarks = bookMarks;
    }

    public DirectMessage bookMarks(Set<BookMark> bookMarks) {
        this.setBookMarks(bookMarks);
        return this;
    }

    public DirectMessage addBookMarks(BookMark bookMark) {
        this.bookMarks.add(bookMark);
        bookMark.setMessage(this);
        return this;
    }

    public DirectMessage removeBookMarks(BookMark bookMark) {
        this.bookMarks.remove(bookMark);
        bookMark.setMessage(null);
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

    public AdminAnnouncement getAdminAnnouncement() {
        return this.adminAnnouncement;
    }

    public void setAdminAnnouncement(AdminAnnouncement adminAnnouncement) {
        if (this.adminAnnouncement != null) {
            this.adminAnnouncement.setAnnouncerMessage(null);
        }
        if (adminAnnouncement != null) {
            adminAnnouncement.setAnnouncerMessage(this);
        }
        this.adminAnnouncement = adminAnnouncement;
    }

    public DirectMessage adminAnnouncement(AdminAnnouncement adminAnnouncement) {
        this.setAdminAnnouncement(adminAnnouncement);
        return this;
    }

    public PurchasedTip getPurchasedTip() {
        return this.purchasedTip;
    }

    public void setPurchasedTip(PurchasedTip purchasedTip) {
        if (this.purchasedTip != null) {
            this.purchasedTip.setMessage(null);
        }
        if (purchasedTip != null) {
            purchasedTip.setMessage(this);
        }
        this.purchasedTip = purchasedTip;
    }

    public DirectMessage purchasedTip(PurchasedTip purchasedTip) {
        this.setPurchasedTip(purchasedTip);
        return this;
    }

    public Long getContentPackageId() {
        return this.contentPackageId;
    }

    public void setContentPackageId(Long contentPackage) {
        this.contentPackageId = contentPackage;
    }

    public Long getResponseToId() {
        return this.responseToId;
    }

    public void setResponseToId(Long directMessage) {
        this.responseToId = directMessage;
    }

    public Long getRepliedStoryId() {
        return this.repliedStoryId;
    }

    public void setRepliedStoryId(Long videoStory) {
        this.repliedStoryId = videoStory;
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
            "}";
    }
}
