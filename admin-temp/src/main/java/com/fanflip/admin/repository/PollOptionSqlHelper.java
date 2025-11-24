package com.monsterdam.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PollOptionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("option_description", table, columnPrefix + "_option_description"));
        columns.add(Column.aliased("vote_count", table, columnPrefix + "_vote_count"));

        columns.add(Column.aliased("poll_id", table, columnPrefix + "_poll_id"));
        return columns;
    }
}
