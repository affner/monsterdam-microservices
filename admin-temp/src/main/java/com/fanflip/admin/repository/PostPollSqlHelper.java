package com.fanflip.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PostPollSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("question", table, columnPrefix + "_question"));
        columns.add(Column.aliased("is_multi_choice", table, columnPrefix + "_is_multi_choice"));
        columns.add(Column.aliased("last_modified_date", table, columnPrefix + "_last_modified_date"));
        columns.add(Column.aliased("end_date", table, columnPrefix + "_end_date"));
        columns.add(Column.aliased("post_poll_duration", table, columnPrefix + "_post_poll_duration"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("last_modified_by", table, columnPrefix + "_last_modified_by"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));

        return columns;
    }
}
