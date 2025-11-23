package com.fanflip.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SingleLiveStreamSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("thumbnail", table, columnPrefix + "_thumbnail"));
        columns.add(Column.aliased("thumbnail_content_type", table, columnPrefix + "_thumbnail_content_type"));
        columns.add(Column.aliased("thumbnail_s_3_key", table, columnPrefix + "_thumbnail_s_3_key"));
        columns.add(Column.aliased("start_time", table, columnPrefix + "_start_time"));
        columns.add(Column.aliased("end_time", table, columnPrefix + "_end_time"));
        columns.add(Column.aliased("live_content", table, columnPrefix + "_live_content"));
        columns.add(Column.aliased("live_content_content_type", table, columnPrefix + "_live_content_content_type"));
        columns.add(Column.aliased("live_content_s_3_key", table, columnPrefix + "_live_content_s_3_key"));
        columns.add(Column.aliased("is_recorded", table, columnPrefix + "_is_recorded"));
        columns.add(Column.aliased("like_count", table, columnPrefix + "_like_count"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("last_modified_date", table, columnPrefix + "_last_modified_date"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("last_modified_by", table, columnPrefix + "_last_modified_by"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));

        return columns;
    }
}
