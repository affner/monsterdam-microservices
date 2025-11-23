package com.fanflip.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PollVoteSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));

        columns.add(Column.aliased("poll_option_id", table, columnPrefix + "_poll_option_id"));
        columns.add(Column.aliased("voting_user_id", table, columnPrefix + "_voting_user_id"));
        return columns;
    }
}
