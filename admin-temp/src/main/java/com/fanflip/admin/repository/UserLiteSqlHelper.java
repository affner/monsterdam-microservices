package com.fanflip.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UserLiteSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("thumbnail", table, columnPrefix + "_thumbnail"));
        columns.add(Column.aliased("thumbnail_content_type", table, columnPrefix + "_thumbnail_content_type"));
        columns.add(Column.aliased("thumbnail_s_3_key", table, columnPrefix + "_thumbnail_s_3_key"));
        columns.add(Column.aliased("birth_date", table, columnPrefix + "_birth_date"));
        columns.add(Column.aliased("gender", table, columnPrefix + "_gender"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("last_modified_date", table, columnPrefix + "_last_modified_date"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("last_modified_by", table, columnPrefix + "_last_modified_by"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("nick_name", table, columnPrefix + "_nick_name"));
        columns.add(Column.aliased("full_name", table, columnPrefix + "_full_name"));
        columns.add(Column.aliased("content_preference", table, columnPrefix + "_content_preference"));

        return columns;
    }
}
