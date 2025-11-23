package com.fanflip.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PurchasedSubscriptionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("last_modified_date", table, columnPrefix + "_last_modified_date"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("last_modified_by", table, columnPrefix + "_last_modified_by"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("end_date", table, columnPrefix + "_end_date"));
        columns.add(Column.aliased("subscription_status", table, columnPrefix + "_subscription_status"));
        columns.add(Column.aliased("viewer_id", table, columnPrefix + "_viewer_id"));
        columns.add(Column.aliased("creator_id", table, columnPrefix + "_creator_id"));

        columns.add(Column.aliased("payment_id", table, columnPrefix + "_payment_id"));
        columns.add(Column.aliased("wallet_transaction_id", table, columnPrefix + "_wallet_transaction_id"));
        columns.add(Column.aliased("creator_earning_id", table, columnPrefix + "_creator_earning_id"));
        columns.add(Column.aliased("subscription_bundle_id", table, columnPrefix + "_subscription_bundle_id"));
        columns.add(Column.aliased("applied_promotion_id", table, columnPrefix + "_applied_promotion_id"));
        columns.add(Column.aliased("viewer_id", table, columnPrefix + "_viewer_id"));
        return columns;
    }
}
