package com.monsterdam.admin.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.PollOption} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollOptionDTO implements Serializable {

    private Long id;

    @Lob
    private String optionDescription;

    @NotNull(message = "must not be null")
    private Integer voteCount;

    private PostPollDTO poll;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionDescription() {
        return optionDescription;
    }

    public void setOptionDescription(String optionDescription) {
        this.optionDescription = optionDescription;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public PostPollDTO getPoll() {
        return poll;
    }

    public void setPoll(PostPollDTO poll) {
        this.poll = poll;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PollOptionDTO)) {
            return false;
        }

        PollOptionDTO pollOptionDTO = (PollOptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pollOptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PollOptionDTO{" +
            "id=" + getId() +
            ", optionDescription='" + getOptionDescription() + "'" +
            ", voteCount=" + getVoteCount() +
            ", poll=" + getPoll() +
            "}";
    }
}
