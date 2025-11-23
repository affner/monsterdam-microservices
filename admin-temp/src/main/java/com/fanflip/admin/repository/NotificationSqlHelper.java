package com.fanflip.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class NotificationSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("read_date", table, columnPrefix + "_read_date"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("last_modified_date", table, columnPrefix + "_last_modified_date"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("last_modified_by", table, columnPrefix + "_last_modified_by"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("post_comment_id", table, columnPrefix + "_post_comment_id"));
        columns.add(Column.aliased("post_feed_id", table, columnPrefix + "_post_feed_id"));
        columns.add(Column.aliased("direct_message_id", table, columnPrefix + "_direct_message_id"));
        columns.add(Column.aliased("user_mention_id", table, columnPrefix + "_user_mention_id"));
        columns.add(Column.aliased("like_mark_id", table, columnPrefix + "_like_mark_id"));

        columns.add(Column.aliased("commented_user_id", table, columnPrefix + "_commented_user_id"));
        columns.add(Column.aliased("messaged_user_id", table, columnPrefix + "_messaged_user_id"));
        columns.add(Column.aliased("mentioner_user_in_post_id", table, columnPrefix + "_mentioner_user_in_post_id"));
        columns.add(Column.aliased("mentioner_user_in_comment_id", table, columnPrefix + "_mentioner_user_in_comment_id"));
        return columns;
    }
}
