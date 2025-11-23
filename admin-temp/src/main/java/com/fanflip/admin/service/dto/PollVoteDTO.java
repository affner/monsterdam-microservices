package com.fanflip.admin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.PollVote} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollVoteDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private PollOptionDTO pollOption;

    private UserProfileDTO votingUser;

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

    public PollOptionDTO getPollOption() {
        return pollOption;
    }

    public void setPollOption(PollOptionDTO pollOption) {
        this.pollOption = pollOption;
    }

    public UserProfileDTO getVotingUser() {
        return votingUser;
    }

    public void setVotingUser(UserProfileDTO votingUser) {
        this.votingUser = votingUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PollVoteDTO)) {
            return false;
        }

        PollVoteDTO pollVoteDTO = (PollVoteDTO) o;
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
            ", pollOption=" + getPollOption() +
            ", votingUser=" + getVotingUser() +
            "}";
    }
}
