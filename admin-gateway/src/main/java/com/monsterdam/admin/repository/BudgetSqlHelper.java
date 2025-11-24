package com.monsterdam.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class BudgetSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("year", table, columnPrefix + "_year"));
        columns.add(Column.aliased("total_budget", table, columnPrefix + "_total_budget"));
        columns.add(Column.aliased("spent_amount", table, columnPrefix + "_spent_amount"));
        columns.add(Column.aliased("remaining_amount", table, columnPrefix + "_remaining_amount"));
        columns.add(Column.aliased("budget_details", table, columnPrefix + "_budget_details"));

        return columns;
    }
}
