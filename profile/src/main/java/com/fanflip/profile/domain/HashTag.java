package com.fanflip.profile.domain;

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
 * A HashTag.
 */
@Entity
@Table(name = "hash_tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "hashtag")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HashTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tag_name", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String tagName;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hashtag")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "hashtag" }, allowSetters = true)
    private Set<PostFeedHashTagRelation> postFeeds = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "hashtags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = {
            "settings",
            "socialNetworks",
            "blockedUbications",
            "followeds",
            "blockedLists",
            "loyaLists",
            "subscribeds",
            "joinedEvents",
            "hashtags",
            "followers",
            "blockers",
            "awards",
            "subscriptions",
        },
        allowSetters = true
    )
    private Set<UserProfile> users = new HashSet<>();

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

    public Set<PostFeedHashTagRelation> getPostFeeds() {
        return this.postFeeds;
    }

    public void setPostFeeds(Set<PostFeedHashTagRelation> postFeedHashTagRelations) {
        if (this.postFeeds != null) {
            this.postFeeds.forEach(i -> i.setHashtag(null));
        }
        if (postFeedHashTagRelations != null) {
            postFeedHashTagRelations.forEach(i -> i.setHashtag(this));
        }
        this.postFeeds = postFeedHashTagRelations;
    }

    public HashTag postFeeds(Set<PostFeedHashTagRelation> postFeedHashTagRelations) {
        this.setPostFeeds(postFeedHashTagRelations);
        return this;
    }

    public HashTag addPostFeeds(PostFeedHashTagRelation postFeedHashTagRelation) {
        this.postFeeds.add(postFeedHashTagRelation);
        postFeedHashTagRelation.setHashtag(this);
        return this;
    }

    public HashTag removePostFeeds(PostFeedHashTagRelation postFeedHashTagRelation) {
        this.postFeeds.remove(postFeedHashTagRelation);
        postFeedHashTagRelation.setHashtag(null);
        return this;
    }

    public Set<UserProfile> getUsers() {
        return this.users;
    }

    public void setUsers(Set<UserProfile> userProfiles) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeHashtags(this));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.addHashtags(this));
        }
        this.users = userProfiles;
    }

    public HashTag users(Set<UserProfile> userProfiles) {
        this.setUsers(userProfiles);
        return this;
    }

    public HashTag addUser(UserProfile userProfile) {
        this.users.add(userProfile);
        userProfile.getHashtags().add(this);
        return this;
    }

    public HashTag removeUser(UserProfile userProfile) {
        this.users.remove(userProfile);
        userProfile.getHashtags().remove(this);
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
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
