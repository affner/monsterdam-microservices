package com.monsterdam.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UserSettingsSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("last_modified_date", table, columnPrefix + "_last_modified_date"));
        columns.add(Column.aliased("dark_mode", table, columnPrefix + "_dark_mode"));
        columns.add(Column.aliased("language", table, columnPrefix + "_language"));
        columns.add(Column.aliased("content_filter", table, columnPrefix + "_content_filter"));
        columns.add(Column.aliased("message_blur_intensity", table, columnPrefix + "_message_blur_intensity"));
        columns.add(Column.aliased("activity_status_visibility", table, columnPrefix + "_activity_status_visibility"));
        columns.add(Column.aliased("two_factor_authentication", table, columnPrefix + "_two_factor_authentication"));
        columns.add(Column.aliased("sessions_active_count", table, columnPrefix + "_sessions_active_count"));
        columns.add(Column.aliased("email_notifications", table, columnPrefix + "_email_notifications"));
        columns.add(Column.aliased("important_subscription_notifications", table, columnPrefix + "_important_subscription_notifications"));
        columns.add(Column.aliased("new_messages", table, columnPrefix + "_new_messages"));
        columns.add(Column.aliased("post_replies", table, columnPrefix + "_post_replies"));
        columns.add(Column.aliased("post_likes", table, columnPrefix + "_post_likes"));
        columns.add(Column.aliased("new_followers", table, columnPrefix + "_new_followers"));
        columns.add(Column.aliased("sms_new_stream", table, columnPrefix + "_sms_new_stream"));
        columns.add(Column.aliased("toast_new_comment", table, columnPrefix + "_toast_new_comment"));
        columns.add(Column.aliased("toast_new_likes", table, columnPrefix + "_toast_new_likes"));
        columns.add(Column.aliased("toast_new_stream", table, columnPrefix + "_toast_new_stream"));
        columns.add(Column.aliased("site_new_comment", table, columnPrefix + "_site_new_comment"));
        columns.add(Column.aliased("site_new_likes", table, columnPrefix + "_site_new_likes"));
        columns.add(Column.aliased("site_discounts_from_followed_users", table, columnPrefix + "_site_discounts_from_followed_users"));
        columns.add(Column.aliased("site_new_stream", table, columnPrefix + "_site_new_stream"));
        columns.add(Column.aliased("site_upcoming_stream_reminders", table, columnPrefix + "_site_upcoming_stream_reminders"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("last_modified_by", table, columnPrefix + "_last_modified_by"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));

        return columns;
    }
}
