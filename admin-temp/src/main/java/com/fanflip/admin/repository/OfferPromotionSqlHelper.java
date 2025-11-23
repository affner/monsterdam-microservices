package com.fanflip.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class OfferPromotionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("free_days_duration", table, columnPrefix + "_free_days_duration"));
        columns.add(Column.aliased("discount_percentage", table, columnPrefix + "_discount_percentage"));
        columns.add(Column.aliased("start_date", table, columnPrefix + "_start_date"));
        columns.add(Column.aliased("end_date", table, columnPrefix + "_end_date"));
        columns.add(Column.aliased("subscriptions_limit", table, columnPrefix + "_subscriptions_limit"));
        columns.add(Column.aliased("link_code", table, columnPrefix + "_link_code"));
        columns.add(Column.aliased("is_finished", table, columnPrefix + "_is_finished"));
        columns.add(Column.aliased("promotion_type", table, columnPrefix + "_promotion_type"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("last_modified_date", table, columnPrefix + "_last_modified_date"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("last_modified_by", table, columnPrefix + "_last_modified_by"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));

        columns.add(Column.aliased("creator_id", table, columnPrefix + "_creator_id"));
        return columns;
    }
}
