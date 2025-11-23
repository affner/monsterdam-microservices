package com.fanflip.profile.domain;

import com.fanflip.profile.domain.enumeration.UserEventStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserEvent.
 */
@Entity
@Table(name = "user_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "creator_event_status")
    private UserEventStatus creatorEventStatus;

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

    @NotNull
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "joinedEvents")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<UserProfile> members = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserEvent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public UserEvent title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public UserEvent description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public UserEvent startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public UserEvent endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public UserEventStatus getCreatorEventStatus() {
        return this.creatorEventStatus;
    }

    public UserEvent creatorEventStatus(UserEventStatus creatorEventStatus) {
        this.setCreatorEventStatus(creatorEventStatus);
        return this;
    }

    public void setCreatorEventStatus(UserEventStatus creatorEventStatus) {
        this.creatorEventStatus = creatorEventStatus;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserEvent createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserEvent lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserEvent createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserEvent lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public UserEvent isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getCreatorId() {
        return this.creatorId;
    }

    public UserEvent creatorId(Long creatorId) {
        this.setCreatorId(creatorId);
        return this;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Set<UserProfile> getMembers() {
        return this.members;
    }

    public void setMembers(Set<UserProfile> userProfiles) {
        if (this.members != null) {
            this.members.forEach(i -> i.removeJoinedEvents(this));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.addJoinedEvents(this));
        }
        this.members = userProfiles;
    }

    public UserEvent members(Set<UserProfile> userProfiles) {
        this.setMembers(userProfiles);
        return this;
    }

    public UserEvent addMembers(UserProfile userProfile) {
        this.members.add(userProfile);
        userProfile.getJoinedEvents().add(this);
        return this;
    }

    public UserEvent removeMembers(UserProfile userProfile) {
        this.members.remove(userProfile);
        userProfile.getJoinedEvents().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEvent)) {
            return false;
        }
        return getId() != null && getId().equals(((UserEvent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserEvent{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", creatorEventStatus='" + getCreatorEventStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", creatorId=" + getCreatorId() +
            "}";
    }
}
