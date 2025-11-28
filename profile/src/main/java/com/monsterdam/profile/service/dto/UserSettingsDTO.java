package com.monsterdam.profile.service.dto;

import com.monsterdam.profile.domain.enumeration.UserLanguage;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.profile.domain.UserSettings} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSettingsDTO implements Serializable {

    private Long id;

    private Instant lastModifiedDate;

    @NotNull
    private Boolean darkMode;

    @NotNull
    private UserLanguage language;

    @NotNull
    private Boolean contentFilter;

    private Integer messageBlurIntensity;

    @NotNull
    private Boolean activityStatusVisibility;

    @NotNull
    private Boolean twoFactorAuthentication;

    private Integer sessionsActiveCount;

    @NotNull
    private Boolean emailNotifications;

    @NotNull
    private Boolean importantSubscriptionNotifications;

    @NotNull
    private Boolean newMessages;

    @NotNull
    private Boolean postReplies;

    @NotNull
    private Boolean postLikes;

    @NotNull
    private Boolean newFollowers;

    @NotNull
    private Boolean smsNewStream;

    @NotNull
    private Boolean toastNewComment;

    @NotNull
    private Boolean toastNewLikes;

    @NotNull
    private Boolean toastNewStream;

    @NotNull
    private Boolean siteNewComment;

    @NotNull
    private Boolean siteNewLikes;

    @NotNull
    private Boolean siteDiscountsFromFollowedUsers;

    @NotNull
    private Boolean siteNewStream;

    @NotNull
    private Boolean siteUpcomingStreamReminders;

    @NotNull
    private Instant createdDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getDarkMode() {
        return darkMode;
    }

    public void setDarkMode(Boolean darkMode) {
        this.darkMode = darkMode;
    }

    public UserLanguage getLanguage() {
        return language;
    }

    public void setLanguage(UserLanguage language) {
        this.language = language;
    }

    public Boolean getContentFilter() {
        return contentFilter;
    }

    public void setContentFilter(Boolean contentFilter) {
        this.contentFilter = contentFilter;
    }

    public Integer getMessageBlurIntensity() {
        return messageBlurIntensity;
    }

    public void setMessageBlurIntensity(Integer messageBlurIntensity) {
        this.messageBlurIntensity = messageBlurIntensity;
    }

    public Boolean getActivityStatusVisibility() {
        return activityStatusVisibility;
    }

    public void setActivityStatusVisibility(Boolean activityStatusVisibility) {
        this.activityStatusVisibility = activityStatusVisibility;
    }

    public Boolean getTwoFactorAuthentication() {
        return twoFactorAuthentication;
    }

    public void setTwoFactorAuthentication(Boolean twoFactorAuthentication) {
        this.twoFactorAuthentication = twoFactorAuthentication;
    }

    public Integer getSessionsActiveCount() {
        return sessionsActiveCount;
    }

    public void setSessionsActiveCount(Integer sessionsActiveCount) {
        this.sessionsActiveCount = sessionsActiveCount;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getImportantSubscriptionNotifications() {
        return importantSubscriptionNotifications;
    }

    public void setImportantSubscriptionNotifications(Boolean importantSubscriptionNotifications) {
        this.importantSubscriptionNotifications = importantSubscriptionNotifications;
    }

    public Boolean getNewMessages() {
        return newMessages;
    }

    public void setNewMessages(Boolean newMessages) {
        this.newMessages = newMessages;
    }

    public Boolean getPostReplies() {
        return postReplies;
    }

    public void setPostReplies(Boolean postReplies) {
        this.postReplies = postReplies;
    }

    public Boolean getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(Boolean postLikes) {
        this.postLikes = postLikes;
    }

    public Boolean getNewFollowers() {
        return newFollowers;
    }

    public void setNewFollowers(Boolean newFollowers) {
        this.newFollowers = newFollowers;
    }

    public Boolean getSmsNewStream() {
        return smsNewStream;
    }

    public void setSmsNewStream(Boolean smsNewStream) {
        this.smsNewStream = smsNewStream;
    }

    public Boolean getToastNewComment() {
        return toastNewComment;
    }

    public void setToastNewComment(Boolean toastNewComment) {
        this.toastNewComment = toastNewComment;
    }

    public Boolean getToastNewLikes() {
        return toastNewLikes;
    }

    public void setToastNewLikes(Boolean toastNewLikes) {
        this.toastNewLikes = toastNewLikes;
    }

    public Boolean getToastNewStream() {
        return toastNewStream;
    }

    public void setToastNewStream(Boolean toastNewStream) {
        this.toastNewStream = toastNewStream;
    }

    public Boolean getSiteNewComment() {
        return siteNewComment;
    }

    public void setSiteNewComment(Boolean siteNewComment) {
        this.siteNewComment = siteNewComment;
    }

    public Boolean getSiteNewLikes() {
        return siteNewLikes;
    }

    public void setSiteNewLikes(Boolean siteNewLikes) {
        this.siteNewLikes = siteNewLikes;
    }

    public Boolean getSiteDiscountsFromFollowedUsers() {
        return siteDiscountsFromFollowedUsers;
    }

    public void setSiteDiscountsFromFollowedUsers(Boolean siteDiscountsFromFollowedUsers) {
        this.siteDiscountsFromFollowedUsers = siteDiscountsFromFollowedUsers;
    }

    public Boolean getSiteNewStream() {
        return siteNewStream;
    }

    public void setSiteNewStream(Boolean siteNewStream) {
        this.siteNewStream = siteNewStream;
    }

    public Boolean getSiteUpcomingStreamReminders() {
        return siteUpcomingStreamReminders;
    }

    public void setSiteUpcomingStreamReminders(Boolean siteUpcomingStreamReminders) {
        this.siteUpcomingStreamReminders = siteUpcomingStreamReminders;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSettingsDTO)) {
            return false;
        }

        UserSettingsDTO userSettingsDTO = (UserSettingsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userSettingsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSettingsDTO{" +
            "id=" + getId() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", darkMode='" + getDarkMode() + "'" +
            ", language='" + getLanguage() + "'" +
            ", contentFilter='" + getContentFilter() + "'" +
            ", messageBlurIntensity=" + getMessageBlurIntensity() +
            ", activityStatusVisibility='" + getActivityStatusVisibility() + "'" +
            ", twoFactorAuthentication='" + getTwoFactorAuthentication() + "'" +
            ", sessionsActiveCount=" + getSessionsActiveCount() +
            ", emailNotifications='" + getEmailNotifications() + "'" +
            ", importantSubscriptionNotifications='" + getImportantSubscriptionNotifications() + "'" +
            ", newMessages='" + getNewMessages() + "'" +
            ", postReplies='" + getPostReplies() + "'" +
            ", postLikes='" + getPostLikes() + "'" +
            ", newFollowers='" + getNewFollowers() + "'" +
            ", smsNewStream='" + getSmsNewStream() + "'" +
            ", toastNewComment='" + getToastNewComment() + "'" +
            ", toastNewLikes='" + getToastNewLikes() + "'" +
            ", toastNewStream='" + getToastNewStream() + "'" +
            ", siteNewComment='" + getSiteNewComment() + "'" +
            ", siteNewLikes='" + getSiteNewLikes() + "'" +
            ", siteDiscountsFromFollowedUsers='" + getSiteDiscountsFromFollowedUsers() + "'" +
            ", siteNewStream='" + getSiteNewStream() + "'" +
            ", siteUpcomingStreamReminders='" + getSiteUpcomingStreamReminders() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
