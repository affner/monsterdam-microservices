package com.monsterdam.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PaymentTransactionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("amount", table, columnPrefix + "_amount"));
        columns.add(Column.aliased("payment_date", table, columnPrefix + "_payment_date"));
        columns.add(Column.aliased("payment_status", table, columnPrefix + "_payment_status"));
        columns.add(Column.aliased("payment_reference", table, columnPrefix + "_payment_reference"));
        columns.add(Column.aliased("cloud_transaction_id", table, columnPrefix + "_cloud_transaction_id"));

        columns.add(Column.aliased("payment_method_id", table, columnPrefix + "_payment_method_id"));
        columns.add(Column.aliased("payment_provider_id", table, columnPrefix + "_payment_provider_id"));
        columns.add(Column.aliased("viewer_id", table, columnPrefix + "_viewer_id"));
        return columns;
    }
}
