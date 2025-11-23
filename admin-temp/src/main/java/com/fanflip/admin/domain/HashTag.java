package com.fanflip.admin.domain;

import com.fanflip.admin.domain.enumeration.HashtagType;
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
 * A HashTag.
 */
@Table("hash_tag")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "hashtag")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HashTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("tag_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String tagName;

    @NotNull(message = "must not be null")
    @Column("hashtag_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private HashtagType hashtagType;

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
    private Set<PostFeed> posts = new HashSet<>();

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
    private Set<UserProfile> profiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HashTag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return this.tagName;
    }

    public HashTag tagName(String tagName) {
        this.setTagName(tagName);
        return this;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public HashtagType getHashtagType() {
        return this.hashtagType;
    }

    public HashTag hashtagType(HashtagType hashtagType) {
        this.setHashtagType(hashtagType);
        return this;
    }

    public void setHashtagType(HashtagType hashtagType) {
        this.hashtagType = hashtagType;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public HashTag createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public HashTag lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public HashTag createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public HashTag lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HashTag isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<PostFeed> getPosts() {
        return this.posts;
    }

    public void setPosts(Set<PostFeed> postFeeds) {
        if (this.posts != null) {
            this.posts.forEach(i -> i.removeHashTags(this));
        }
        if (postFeeds != null) {
            postFeeds.forEach(i -> i.addHashTags(this));
        }
        this.posts = postFeeds;
    }

    public HashTag posts(Set<PostFeed> postFeeds) {
        this.setPosts(postFeeds);
        return this;
    }

    public HashTag addPosts(PostFeed postFeed) {
        this.posts.add(postFeed);
        postFeed.getHashTags().add(this);
        return this;
    }

    public HashTag removePosts(PostFeed postFeed) {
        this.posts.remove(postFeed);
        postFeed.getHashTags().remove(this);
        return this;
    }

    public Set<UserProfile> getProfiles() {
        return this.profiles;
    }

    public void setProfiles(Set<UserProfile> userProfiles) {
        if (this.profiles != null) {
            this.profiles.forEach(i -> i.removeHashTags(this));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.addHashTags(this));
        }
        this.profiles = userProfiles;
    }

    public HashTag profiles(Set<UserProfile> userProfiles) {
        this.setProfiles(userProfiles);
        return this;
    }

    public HashTag addProfiles(UserProfile userProfile) {
        this.profiles.add(userProfile);
        userProfile.getHashTags().add(this);
        return this;
    }

    public HashTag removeProfiles(UserProfile userProfile) {
        this.profiles.remove(userProfile);
        userProfile.getHashTags().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HashTag)) {
            return false;
        }
        return getId() != null && getId().equals(((HashTag) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HashTag{" +
            "id=" + getId() +
            ", tagName='" + getTagName() + "'" +
            ", hashtagType='" + getHashtagType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
