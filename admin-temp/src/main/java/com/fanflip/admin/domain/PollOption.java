package com.monsterdam.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PollOption.
 */
@Table("poll_option")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "polloption")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("option_description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String optionDescription;

    @NotNull(message = "must not be null")
    @Column("vote_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer voteCount;

    @Transient
    @JsonIgnoreProperties(value = { "options", "post" }, allowSetters = true)
    private PostPoll poll;

    @Transient
    @JsonIgnoreProperties(value = { "pollOption", "votingUser" }, allowSetters = true)
    private Set<PollVote> votes = new HashSet<>();

    @Column("poll_id")
    private Long pollId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PollOption id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionDescription() {
        return this.optionDescription;
    }

    public PollOption optionDescription(String optionDescription) {
        this.setOptionDescription(optionDescription);
        return this;
    }

    public void setOptionDescription(String optionDescription) {
        this.optionDescription = optionDescription;
    }

    public Integer getVoteCount() {
        return this.voteCount;
    }

    public PollOption voteCount(Integer voteCount) {
        this.setVoteCount(voteCount);
        return this;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public PostPoll getPoll() {
        return this.poll;
    }

    public void setPoll(PostPoll postPoll) {
        this.poll = postPoll;
        this.pollId = postPoll != null ? postPoll.getId() : null;
    }

    public PollOption poll(PostPoll postPoll) {
        this.setPoll(postPoll);
        return this;
    }

    public Set<PollVote> getVotes() {
        return this.votes;
    }

    public void setVotes(Set<PollVote> pollVotes) {
        if (this.votes != null) {
            this.votes.forEach(i -> i.setPollOption(null));
        }
        if (pollVotes != null) {
            pollVotes.forEach(i -> i.setPollOption(this));
        }
        this.votes = pollVotes;
    }

    public PollOption votes(Set<PollVote> pollVotes) {
        this.setVotes(pollVotes);
        return this;
    }

    public PollOption addVotes(PollVote pollVote) {
        this.votes.add(pollVote);
        pollVote.setPollOption(this);
        return this;
    }

    public PollOption removeVotes(PollVote pollVote) {
        this.votes.remove(pollVote);
        pollVote.setPollOption(null);
        return this;
    }

    public Long getPollId() {
        return this.pollId;
    }

    public void setPollId(Long postPoll) {
        this.pollId = postPoll;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PollOption)) {
            return false;
        }
        return getId() != null && getId().equals(((PollOption) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PollOption{" +
            "id=" + getId() +
            ", optionDescription='" + getOptionDescription() + "'" +
            ", voteCount=" + getVoteCount() +
            "}";
    }
}
