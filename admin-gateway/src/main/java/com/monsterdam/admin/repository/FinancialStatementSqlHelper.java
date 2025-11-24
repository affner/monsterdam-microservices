package com.monsterdam.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class FinancialStatementSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("statement_type", table, columnPrefix + "_statement_type"));
        columns.add(Column.aliased("period_start_date", table, columnPrefix + "_period_start_date"));
        columns.add(Column.aliased("period_end_date", table, columnPrefix + "_period_end_date"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));

        return columns;
    }
}
