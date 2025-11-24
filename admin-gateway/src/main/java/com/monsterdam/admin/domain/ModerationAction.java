package com.monsterdam.admin.domain;

import com.monsterdam.admin.domain.enumeration.ModerationActionType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ModerationAction.
 */
@Table("moderation_action")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "moderationaction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModerationAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("action_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ModerationActionType actionType;

    @Size(max = 255)
    @Column("reason")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String reason;

    @Column("action_date")
    private Instant actionDate;

    @Column("duration_days")
    private Duration durationDays;

    @Transient
    private AssistanceTicket assistanceTicket;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ModerationAction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ModerationActionType getActionType() {
        return this.actionType;
    }

    public ModerationAction actionType(ModerationActionType actionType) {
        this.setActionType(actionType);
        return this;
    }

    public void setActionType(ModerationActionType actionType) {
        this.actionType = actionType;
    }

    public String getReason() {
        return this.reason;
    }

    public ModerationAction reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getActionDate() {
        return this.actionDate;
    }

    public ModerationAction actionDate(Instant actionDate) {
        this.setActionDate(actionDate);
        return this;
    }

    public void setActionDate(Instant actionDate) {
        this.actionDate = actionDate;
    }

    public Duration getDurationDays() {
        return this.durationDays;
    }

    public ModerationAction durationDays(Duration durationDays) {
        this.setDurationDays(durationDays);
        return this;
    }

    public void setDurationDays(Duration durationDays) {
        this.durationDays = durationDays;
    }

    public AssistanceTicket getAssistanceTicket() {
        return this.assistanceTicket;
    }

    public void setAssistanceTicket(AssistanceTicket assistanceTicket) {
        if (this.assistanceTicket != null) {
            this.assistanceTicket.setModerationAction(null);
        }
        if (assistanceTicket != null) {
            assistanceTicket.setModerationAction(this);
        }
        this.assistanceTicket = assistanceTicket;
    }

    public ModerationAction assistanceTicket(AssistanceTicket assistanceTicket) {
        this.setAssistanceTicket(assistanceTicket);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModerationAction)) {
            return false;
        }
        return getId() != null && getId().equals(((ModerationAction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModerationAction{" +
            "id=" + getId() +
            ", actionType='" + getActionType() + "'" +
            ", reason='" + getReason() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", durationDays='" + getDurationDays() + "'" +
            "}";
    }
}
