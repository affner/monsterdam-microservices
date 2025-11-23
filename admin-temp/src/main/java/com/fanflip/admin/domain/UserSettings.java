package com.fanflip.admin.domain;

import com.fanflip.admin.domain.enumeration.UserLanguage;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UserSettings.
 */
@Table("user_settings")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "usersettings")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("last_modified_date")
    private Instant lastModifiedDate;

    @NotNull(message = "must not be null")
    @Column("dark_mode")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean darkMode;

    @NotNull(message = "must not be null")
    @Column("language")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private UserLanguage language;

    @NotNull(message = "must not be null")
    @Column("content_filter")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean contentFilter;

    @Column("message_blur_intensity")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer messageBlurIntensity;

    @NotNull(message = "must not be null")
    @Column("activity_status_visibility")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean activityStatusVisibility;

    @NotNull(message = "must not be null")
    @Column("two_factor_authentication")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean twoFactorAuthentication;

    @Column("sessions_active_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer sessionsActiveCount;

    @NotNull(message = "must not be null")
    @Column("email_notifications")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean emailNotifications;

    @NotNull(message = "must not be null")
    @Column("important_subscription_notifications")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean importantSubscriptionNotifications;

    @NotNull(message = "must not be null")
    @Column("new_messages")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean newMessages;

    @NotNull(message = "must not be null")
    @Column("post_replies")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean postReplies;

    @NotNull(message = "must not be null")
    @Column("post_likes")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean postLikes;

    @NotNull(message = "must not be null")
    @Column("new_followers")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean newFollowers;

    @NotNull(message = "must not be null")
    @Column("sms_new_stream")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean smsNewStream;

    @NotNull(message = "must not be null")
    @Column("toast_new_comment")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean toastNewComment;

    @NotNull(message = "must not be null")
    @Column("toast_new_likes")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean toastNewLikes;

    @NotNull(message = "must not be null")
    @Column("toast_new_stream")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean toastNewStream;

    @NotNull(message = "must not be null")
    @Column("site_new_comment")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean siteNewComment;

    @NotNull(message = "must not be null")
    @Column("site_new_likes")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean siteNewLikes;

    @NotNull(message = "must not be null")
    @Column("site_discounts_from_followed_users")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean siteDiscountsFromFollowedUsers;

    @NotNull(message = "must not be null")
    @Column("site_new_stream")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean siteNewStream;

    @NotNull(message = "must not be null")
    @Column("site_upcoming_stream_reminders")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean siteUpcomingStreamReminders;

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
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserSettings id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserSettings lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getDarkMode() {
        return this.darkMode;
    }

    public UserSettings darkMode(Boolean darkMode) {
        this.setDarkMode(darkMode);
        return this;
    }

    public void setDarkMode(Boolean darkMode) {
        this.darkMode = darkMode;
    }

    public UserLanguage getLanguage() {
        return this.language;
    }

    public UserSettings language(UserLanguage language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(UserLanguage language) {
        this.language = language;
    }

    public Boolean getContentFilter() {
        return this.contentFilter;
    }

    public UserSettings contentFilter(Boolean contentFilter) {
        this.setContentFilter(contentFilter);
        return this;
    }

    public void setContentFilter(Boolean contentFilter) {
        this.contentFilter = contentFilter;
    }

    public Integer getMessageBlurIntensity() {
        return this.messageBlurIntensity;
    }

    public UserSettings messageBlurIntensity(Integer messageBlurIntensity) {
        this.setMessageBlurIntensity(messageBlurIntensity);
        return this;
    }

    public void setMessageBlurIntensity(Integer messageBlurIntensity) {
        this.messageBlurIntensity = messageBlurIntensity;
    }

    public Boolean getActivityStatusVisibility() {
        return this.activityStatusVisibility;
    }

    public UserSettings activityStatusVisibility(Boolean activityStatusVisibility) {
        this.setActivityStatusVisibility(activityStatusVisibility);
        return this;
    }

    public void setActivityStatusVisibility(Boolean activityStatusVisibility) {
        this.activityStatusVisibility = activityStatusVisibility;
    }

    public Boolean getTwoFactorAuthentication() {
        return this.twoFactorAuthentication;
    }

    public UserSettings twoFactorAuthentication(Boolean twoFactorAuthentication) {
        this.setTwoFactorAuthentication(twoFactorAuthentication);
        return this;
    }

    public void setTwoFactorAuthentication(Boolean twoFactorAuthentication) {
        this.twoFactorAuthentication = twoFactorAuthentication;
    }

    public Integer getSessionsActiveCount() {
        return this.sessionsActiveCount;
    }

    public UserSettings sessionsActiveCount(Integer sessionsActiveCount) {
        this.setSessionsActiveCount(sessionsActiveCount);
        return this;
    }

    public void setSessionsActiveCount(Integer sessionsActiveCount) {
        this.sessionsActiveCount = sessionsActiveCount;
    }

    public Boolean getEmailNotifications() {
        return this.emailNotifications;
    }

    public UserSettings emailNotifications(Boolean emailNotifications) {
        this.setEmailNotifications(emailNotifications);
        return this;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getImportantSubscriptionNotifications() {
        return this.importantSubscriptionNotifications;
    }

    public UserSettings importantSubscriptionNotifications(Boolean importantSubscriptionNotifications) {
        this.setImportantSubscriptionNotifications(importantSubscriptionNotifications);
        return this;
    }

    public void setImportantSubscriptionNotifications(Boolean importantSubscriptionNotifications) {
        this.importantSubscriptionNotifications = importantSubscriptionNotifications;
    }

    public Boolean getNewMessages() {
        return this.newMessages;
    }

    public UserSettings newMessages(Boolean newMessages) {
        this.setNewMessages(newMessages);
        return this;
    }

    public void setNewMessages(Boolean newMessages) {
        this.newMessages = newMessages;
    }

    public Boolean getPostReplies() {
        return this.postReplies;
    }

    public UserSettings postReplies(Boolean postReplies) {
        this.setPostReplies(postReplies);
        return this;
    }

    public void setPostReplies(Boolean postReplies) {
        this.postReplies = postReplies;
    }

    public Boolean getPostLikes() {
        return this.postLikes;
    }

    public UserSettings postLikes(Boolean postLikes) {
        this.setPostLikes(postLikes);
        return this;
    }

    public void setPostLikes(Boolean postLikes) {
        this.postLikes = postLikes;
    }

    public Boolean getNewFollowers() {
        return this.newFollowers;
    }

    public UserSettings newFollowers(Boolean newFollowers) {
        this.setNewFollowers(newFollowers);
        return this;
    }

    public void setNewFollowers(Boolean newFollowers) {
        this.newFollowers = newFollowers;
    }

    public Boolean getSmsNewStream() {
        return this.smsNewStream;
    }

    public UserSettings smsNewStream(Boolean smsNewStream) {
        this.setSmsNewStream(smsNewStream);
        return this;
    }

    public void setSmsNewStream(Boolean smsNewStream) {
        this.smsNewStream = smsNewStream;
    }

    public Boolean getToastNewComment() {
        return this.toastNewComment;
    }

    public UserSettings toastNewComment(Boolean toastNewComment) {
        this.setToastNewComment(toastNewComment);
        return this;
    }

    public void setToastNewComment(Boolean toastNewComment) {
        this.toastNewComment = toastNewComment;
    }

    public Boolean getToastNewLikes() {
        return this.toastNewLikes;
    }

    public UserSettings toastNewLikes(Boolean toastNewLikes) {
        this.setToastNewLikes(toastNewLikes);
        return this;
    }

    public void setToastNewLikes(Boolean toastNewLikes) {
        this.toastNewLikes = toastNewLikes;
    }

    public Boolean getToastNewStream() {
        return this.toastNewStream;
    }

    public UserSettings toastNewStream(Boolean toastNewStream) {
        this.setToastNewStream(toastNewStream);
        return this;
    }

    public void setToastNewStream(Boolean toastNewStream) {
        this.toastNewStream = toastNewStream;
    }

    public Boolean getSiteNewComment() {
        return this.siteNewComment;
    }

    public UserSettings siteNewComment(Boolean siteNewComment) {
        this.setSiteNewComment(siteNewComment);
        return this;
    }

    public void setSiteNewComment(Boolean siteNewComment) {
        this.siteNewComment = siteNewComment;
    }

    public Boolean getSiteNewLikes() {
        return this.siteNewLikes;
    }

    public UserSettings siteNewLikes(Boolean siteNewLikes) {
        this.setSiteNewLikes(siteNewLikes);
        return this;
    }

    public void setSiteNewLikes(Boolean siteNewLikes) {
        this.siteNewLikes = siteNewLikes;
    }

    public Boolean getSiteDiscountsFromFollowedUsers() {
        return this.siteDiscountsFromFollowedUsers;
    }

    public UserSettings siteDiscountsFromFollowedUsers(Boolean siteDiscountsFromFollowedUsers) {
        this.setSiteDiscountsFromFollowedUsers(siteDiscountsFromFollowedUsers);
        return this;
    }

    public void setSiteDiscountsFromFollowedUsers(Boolean siteDiscountsFromFollowedUsers) {
        this.siteDiscountsFromFollowedUsers = siteDiscountsFromFollowedUsers;
    }

    public Boolean getSiteNewStream() {
        return this.siteNewStream;
    }

    public UserSettings siteNewStream(Boolean siteNewStream) {
        this.setSiteNewStream(siteNewStream);
        return this;
    }

    public void setSiteNewStream(Boolean siteNewStream) {
        this.siteNewStream = siteNewStream;
    }

    public Boolean getSiteUpcomingStreamReminders() {
        return this.siteUpcomingStreamReminders;
    }

    public UserSettings siteUpcomingStreamReminders(Boolean siteUpcomingStreamReminders) {
        this.setSiteUpcomingStreamReminders(siteUpcomingStreamReminders);
        return this;
    }

    public void setSiteUpcomingStreamReminders(Boolean siteUpcomingStreamReminders) {
        this.siteUpcomingStreamReminders = siteUpcomingStreamReminders;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserSettings createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserSettings createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserSettings lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public UserSettings isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        if (this.userProfile != null) {
            this.userProfile.setSettings(null);
        }
        if (userProfile != null) {
            userProfile.setSettings(this);
        }
        this.userProfile = userProfile;
    }

    public UserSettings userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSettings)) {
            return false;
        }
        return getId() != null && getId().equals(((UserSettings) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSettings{" +
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
