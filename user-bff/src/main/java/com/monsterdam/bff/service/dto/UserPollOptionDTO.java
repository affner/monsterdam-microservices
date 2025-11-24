package com.monsterdam.bff.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for BFF data handling.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPollOptionDTO implements Serializable {

    private Long id;

    @Lob
    private String optionDescription;

    @NotNull
    private Integer voteCount;

    private UserPostPollDTO poll;

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

    public UserPostPollDTO getPoll() {
        return poll;
    }

    public void setPoll(UserPostPollDTO poll) {
        this.poll = poll;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPollOptionDTO)) {
            return false;
        }

        UserPollOptionDTO pollOptionDTO = (UserPollOptionDTO) o;
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
