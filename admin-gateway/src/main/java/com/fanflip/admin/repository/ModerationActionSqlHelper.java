package com.fanflip.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ModerationActionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("action_type", table, columnPrefix + "_action_type"));
        columns.add(Column.aliased("reason", table, columnPrefix + "_reason"));
        columns.add(Column.aliased("action_date", table, columnPrefix + "_action_date"));
        columns.add(Column.aliased("duration_days", table, columnPrefix + "_duration_days"));

        return columns;
    }
}
