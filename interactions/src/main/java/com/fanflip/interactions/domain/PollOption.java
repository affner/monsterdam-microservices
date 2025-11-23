package com.fanflip.interactions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PollOption.
 */
@Entity
@Table(name = "poll_option")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "option_description", nullable = false)
    private String optionDescription;

    @NotNull
    @Column(name = "vote_count", nullable = false)
    private Integer voteCount;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "options", "post" }, allowSetters = true)
    private PostPoll poll;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pollOption")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pollOption" }, allowSetters = true)
    private Set<PollVote> votes = new HashSet<>();

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
