package com.fanflip.interactions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PostPoll.
 */
@Entity
@Table(name = "post_poll")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostPoll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "question", nullable = false)
    private String question;

    @NotNull
    @Column(name = "is_multi_choice", nullable = false)
    private Boolean isMultiChoice;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "post_poll_duration", nullable = false)
    private Duration postPollDuration;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "poll")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "poll", "votes" }, allowSetters = true)
    private Set<PollOption> options = new HashSet<>();

    @JsonIgnoreProperties(value = { "poll", "comments", "commentMentions" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "poll")
    private PostFeed post;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PostPoll id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return this.question;
    }

    public PostPoll question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getIsMultiChoice() {
        return this.isMultiChoice;
    }

    public PostPoll isMultiChoice(Boolean isMultiChoice) {
        this.setIsMultiChoice(isMultiChoice);
        return this;
    }

    public void setIsMultiChoice(Boolean isMultiChoice) {
        this.isMultiChoice = isMultiChoice;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public PostPoll lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public PostPoll endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Duration getPostPollDuration() {
        return this.postPollDuration;
    }

    public PostPoll postPollDuration(Duration postPollDuration) {
        this.setPostPollDuration(postPollDuration);
        return this;
    }

    public void setPostPollDuration(Duration postPollDuration) {
        this.postPollDuration = postPollDuration;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PostPoll createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PostPoll createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PostPoll lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public PostPoll isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<PollOption> getOptions() {
        return this.options;
    }

    public void setOptions(Set<PollOption> pollOptions) {
        if (this.options != null) {
            this.options.forEach(i -> i.setPoll(null));
        }
        if (pollOptions != null) {
            pollOptions.forEach(i -> i.setPoll(this));
        }
        this.options = pollOptions;
    }

    public PostPoll options(Set<PollOption> pollOptions) {
        this.setOptions(pollOptions);
        return this;
    }

    public PostPoll addOptions(PollOption pollOption) {
        this.options.add(pollOption);
        pollOption.setPoll(this);
        return this;
    }

    public PostPoll removeOptions(PollOption pollOption) {
        this.options.remove(pollOption);
        pollOption.setPoll(null);
        return this;
    }

    public PostFeed getPost() {
        return this.post;
    }

    public void setPost(PostFeed postFeed) {
        if (this.post != null) {
            this.post.setPoll(null);
        }
        if (postFeed != null) {
            postFeed.setPoll(this);
        }
        this.post = postFeed;
    }

    public PostPoll post(PostFeed postFeed) {
        this.setPost(postFeed);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostPoll)) {
            return false;
        }
        return getId() != null && getId().equals(((PostPoll) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostPoll{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", isMultiChoice='" + getIsMultiChoice() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", postPollDuration='" + getPostPollDuration() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
