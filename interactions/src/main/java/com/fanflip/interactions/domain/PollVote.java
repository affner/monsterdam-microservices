package com.fanflip.interactions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PollVote.
 */
@Entity
@Table(name = "poll_vote")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollVote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @NotNull
    @Column(name = "voting_user_id", nullable = false)
    private Long votingUserId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "poll", "votes" }, allowSetters = true)
    private PollOption pollOption;

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

    public Long getVotingUserId() {
        return this.votingUserId;
    }

    public PollVote votingUserId(Long votingUserId) {
        this.setVotingUserId(votingUserId);
        return this;
    }

    public void setVotingUserId(Long votingUserId) {
        this.votingUserId = votingUserId;
    }

    public PollOption getPollOption() {
        return this.pollOption;
    }

    public void setPollOption(PollOption pollOption) {
        this.pollOption = pollOption;
    }

    public PollVote pollOption(PollOption pollOption) {
        this.setPollOption(pollOption);
        return this;
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
            ", votingUserId=" + getVotingUserId() +
            "}";
    }
}
