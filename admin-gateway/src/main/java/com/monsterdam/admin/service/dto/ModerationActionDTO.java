package com.monsterdam.admin.service.dto;

import com.monsterdam.admin.domain.enumeration.ModerationActionType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.ModerationAction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModerationActionDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private ModerationActionType actionType;

    @Size(max = 255)
    private String reason;

    private Instant actionDate;

    private Duration durationDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ModerationActionType getActionType() {
        return actionType;
    }

    public void setActionType(ModerationActionType actionType) {
        this.actionType = actionType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getActionDate() {
        return actionDate;
    }

    public void setActionDate(Instant actionDate) {
        this.actionDate = actionDate;
    }

    public Duration getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Duration durationDays) {
        this.durationDays = durationDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModerationActionDTO)) {
            return false;
        }

        ModerationActionDTO moderationActionDTO = (ModerationActionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moderationActionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModerationActionDTO{" +
            "id=" + getId() +
            ", actionType='" + getActionType() + "'" +
            ", reason='" + getReason() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", durationDays='" + getDurationDays() + "'" +
            "}";
    }
}
