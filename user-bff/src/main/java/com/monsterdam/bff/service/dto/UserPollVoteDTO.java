package com.monsterdam.bff.service.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for BFF data handling.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPollVoteDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdDate;

    @NotNull
    private Long votingUserId;

    private UserPollOptionDTO pollOption;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getVotingUserId() {
        return votingUserId;
    }

    public void setVotingUserId(Long votingUserId) {
        this.votingUserId = votingUserId;
    }

    public UserPollOptionDTO getPollOption() {
        return pollOption;
    }

    public void setPollOption(UserPollOptionDTO pollOption) {
        this.pollOption = pollOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPollVoteDTO)) {
            return false;
        }

        UserPollVoteDTO pollVoteDTO = (UserPollVoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pollVoteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PollVoteDTO{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", votingUserId=" + getVotingUserId() +
            ", pollOption=" + getPollOption() +
            "}";
    }
}
