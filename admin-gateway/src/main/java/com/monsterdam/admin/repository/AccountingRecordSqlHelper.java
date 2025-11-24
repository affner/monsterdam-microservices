package com.monsterdam.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AccountingRecordSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("date", table, columnPrefix + "_date"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("debit", table, columnPrefix + "_debit"));
        columns.add(Column.aliased("credit", table, columnPrefix + "_credit"));
        columns.add(Column.aliased("balance", table, columnPrefix + "_balance"));
        columns.add(Column.aliased("account_type", table, columnPrefix + "_account_type"));
        columns.add(Column.aliased("payment_id", table, columnPrefix + "_payment_id"));

        columns.add(Column.aliased("budget_id", table, columnPrefix + "_budget_id"));
        columns.add(Column.aliased("asset_id", table, columnPrefix + "_asset_id"));
        columns.add(Column.aliased("liability_id", table, columnPrefix + "_liability_id"));
        return columns;
    }
}
