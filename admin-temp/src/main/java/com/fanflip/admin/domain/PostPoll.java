package com.fanflip.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PostPoll.
 */
@Table("post_poll")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "postpoll")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostPoll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("question")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String question;

    @NotNull(message = "must not be null")
    @Column("is_multi_choice")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isMultiChoice;

    @Column("last_modified_date")
    private Instant lastModifiedDate;

    @NotNull(message = "must not be null")
    @Column("end_date")
    private LocalDate endDate;

    @NotNull(message = "must not be null")
    @Column("post_poll_duration")
    private Duration postPollDuration;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private Instant createdDate;

    @Column("created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    @Column("last_modified_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lastModifiedBy;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isDeleted;

    @Transient
    @JsonIgnoreProperties(value = { "poll", "votes" }, allowSetters = true)
    private Set<PollOption> options = new HashSet<>();

    @Transient
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
