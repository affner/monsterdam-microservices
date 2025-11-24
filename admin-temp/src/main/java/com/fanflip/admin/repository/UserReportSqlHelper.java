package com.monsterdam.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UserReportSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("report_description", table, columnPrefix + "_report_description"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("last_modified_date", table, columnPrefix + "_last_modified_date"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("last_modified_by", table, columnPrefix + "_last_modified_by"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("report_category", table, columnPrefix + "_report_category"));

        columns.add(Column.aliased("ticket_id", table, columnPrefix + "_ticket_id"));
        columns.add(Column.aliased("reporter_id", table, columnPrefix + "_reporter_id"));
        columns.add(Column.aliased("reported_id", table, columnPrefix + "_reported_id"));
        columns.add(Column.aliased("story_id", table, columnPrefix + "_story_id"));
        columns.add(Column.aliased("video_id", table, columnPrefix + "_video_id"));
        columns.add(Column.aliased("photo_id", table, columnPrefix + "_photo_id"));
        columns.add(Column.aliased("audio_id", table, columnPrefix + "_audio_id"));
        columns.add(Column.aliased("live_stream_id", table, columnPrefix + "_live_stream_id"));
        columns.add(Column.aliased("message_id", table, columnPrefix + "_message_id"));
        columns.add(Column.aliased("post_id", table, columnPrefix + "_post_id"));
        columns.add(Column.aliased("post_comment_id", table, columnPrefix + "_post_comment_id"));
        return columns;
    }
}
