package com.monsterdam.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PostCommentSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("comment_content", table, columnPrefix + "_comment_content"));
        columns.add(Column.aliased("like_count", table, columnPrefix + "_like_count"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("last_modified_date", table, columnPrefix + "_last_modified_date"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("last_modified_by", table, columnPrefix + "_last_modified_by"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));

        columns.add(Column.aliased("post_id", table, columnPrefix + "_post_id"));
        columns.add(Column.aliased("response_to_id", table, columnPrefix + "_response_to_id"));
        columns.add(Column.aliased("commenter_id", table, columnPrefix + "_commenter_id"));
        return columns;
    }
}
