package com.fanflip.interactions.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.interactions.domain.PollVote} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollVoteDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdDate;

    @NotNull
    private Long votingUserId;

    private PollOptionDTO pollOption;

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

    public PollOptionDTO getPollOption() {
        return pollOption;
    }

    public void setPollOption(PollOptionDTO pollOption) {
        this.pollOption = pollOption;
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
            ", votingUserId=" + getVotingUserId() +
            ", pollOption=" + getPollOption() +
            "}";
    }
}
