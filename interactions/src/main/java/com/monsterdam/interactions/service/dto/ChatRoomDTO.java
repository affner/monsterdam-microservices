package com.monsterdam.interactions.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.monsterdam.interactions.domain.ChatRoom} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatRoomDTO implements Serializable {

    private Long id;

    private String lastAction;

    private Instant lastConnectionDate;

    private Boolean muted;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    private Long participantUserId;

    private Set<DirectMessageDTO> sentMessages = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public Instant getLastConnectionDate() {
        return lastConnectionDate;
    }

    public void setLastConnectionDate(Instant lastConnectionDate) {
        this.lastConnectionDate = lastConnectionDate;
    }

    public Boolean getMuted() {
        return muted;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getParticipantUserId() {
        return participantUserId;
    }

    public void setParticipantUserId(Long participantUserId) {
        this.participantUserId = participantUserId;
    }

    public Set<DirectMessageDTO> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(Set<DirectMessageDTO> sentMessages) {
        this.sentMessages = sentMessages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatRoomDTO)) {
            return false;
        }

        ChatRoomDTO chatRoomDTO = (ChatRoomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chatRoomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatRoomDTO{" +
            "id=" + getId() +
            ", lastAction='" + getLastAction() + "'" +
            ", lastConnectionDate='" + getLastConnectionDate() + "'" +
            ", muted='" + getMuted() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", participantUserId=" + getParticipantUserId() +
            ", sentMessages=" + getSentMessages() +
            "}";
    }
}
