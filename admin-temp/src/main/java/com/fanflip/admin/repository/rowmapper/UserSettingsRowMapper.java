package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.UserSettings;
import com.fanflip.admin.domain.enumeration.UserLanguage;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserSettings}, with proper type conversions.
 */
@Service
public class UserSettingsRowMapper implements BiFunction<Row, String, UserSettings> {

    private final ColumnConverter converter;

    public UserSettingsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserSettings} stored in the database.
     */
    @Override
    public UserSettings apply(Row row, String prefix) {
        UserSettings entity = new UserSettings();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setDarkMode(converter.fromRow(row, prefix + "_dark_mode", Boolean.class));
        entity.setLanguage(converter.fromRow(row, prefix + "_language", UserLanguage.class));
        entity.setContentFilter(converter.fromRow(row, prefix + "_content_filter", Boolean.class));
        entity.setMessageBlurIntensity(converter.fromRow(row, prefix + "_message_blur_intensity", Integer.class));
        entity.setActivityStatusVisibility(converter.fromRow(row, prefix + "_activity_status_visibility", Boolean.class));
        entity.setTwoFactorAuthentication(converter.fromRow(row, prefix + "_two_factor_authentication", Boolean.class));
        entity.setSessionsActiveCount(converter.fromRow(row, prefix + "_sessions_active_count", Integer.class));
        entity.setEmailNotifications(converter.fromRow(row, prefix + "_email_notifications", Boolean.class));
        entity.setImportantSubscriptionNotifications(
            converter.fromRow(row, prefix + "_important_subscription_notifications", Boolean.class)
        );
        entity.setNewMessages(converter.fromRow(row, prefix + "_new_messages", Boolean.class));
        entity.setPostReplies(converter.fromRow(row, prefix + "_post_replies", Boolean.class));
        entity.setPostLikes(converter.fromRow(row, prefix + "_post_likes", Boolean.class));
        entity.setNewFollowers(converter.fromRow(row, prefix + "_new_followers", Boolean.class));
        entity.setSmsNewStream(converter.fromRow(row, prefix + "_sms_new_stream", Boolean.class));
        entity.setToastNewComment(converter.fromRow(row, prefix + "_toast_new_comment", Boolean.class));
        entity.setToastNewLikes(converter.fromRow(row, prefix + "_toast_new_likes", Boolean.class));
        entity.setToastNewStream(converter.fromRow(row, prefix + "_toast_new_stream", Boolean.class));
        entity.setSiteNewComment(converter.fromRow(row, prefix + "_site_new_comment", Boolean.class));
        entity.setSiteNewLikes(converter.fromRow(row, prefix + "_site_new_likes", Boolean.class));
        entity.setSiteDiscountsFromFollowedUsers(converter.fromRow(row, prefix + "_site_discounts_from_followed_users", Boolean.class));
        entity.setSiteNewStream(converter.fromRow(row, prefix + "_site_new_stream", Boolean.class));
        entity.setSiteUpcomingStreamReminders(converter.fromRow(row, prefix + "_site_upcoming_stream_reminders", Boolean.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
