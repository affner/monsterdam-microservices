package com.fanflip.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PollVote.
 */
@Table("poll_vote")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "pollvote")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollVote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private Instant createdDate;

    @Transient
    @JsonIgnoreProperties(value = { "poll", "votes" }, allowSetters = true)
    private PollOption pollOption;

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
    private UserProfile votingUser;

    @Column("poll_option_id")
    private Long pollOptionId;

    @Column("voting_user_id")
    private Long votingUserId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PollVote id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PollVote createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public PollOption getPollOption() {
        return this.pollOption;
    }

    public void setPollOption(PollOption pollOption) {
        this.pollOption = pollOption;
        this.pollOptionId = pollOption != null ? pollOption.getId() : null;
    }

    public PollVote pollOption(PollOption pollOption) {
        this.setPollOption(pollOption);
        return this;
    }

    public UserProfile getVotingUser() {
        return this.votingUser;
    }

    public void setVotingUser(UserProfile userProfile) {
        this.votingUser = userProfile;
        this.votingUserId = userProfile != null ? userProfile.getId() : null;
    }

    public PollVote votingUser(UserProfile userProfile) {
        this.setVotingUser(userProfile);
        return this;
    }

    public Long getPollOptionId() {
        return this.pollOptionId;
    }

    public void setPollOptionId(Long pollOption) {
        this.pollOptionId = pollOption;
    }

    public Long getVotingUserId() {
        return this.votingUserId;
    }

    public void setVotingUserId(Long userProfile) {
        this.votingUserId = userProfile;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PollVote)) {
            return false;
        }
        return getId() != null && getId().equals(((PollVote) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PollVote{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
