package com.fanflip.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class IdentityDocumentSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("document_name", table, columnPrefix + "_document_name"));
        columns.add(Column.aliased("document_description", table, columnPrefix + "_document_description"));
        columns.add(Column.aliased("document_status", table, columnPrefix + "_document_status"));
        columns.add(Column.aliased("document_type", table, columnPrefix + "_document_type"));
        columns.add(Column.aliased("file_document", table, columnPrefix + "_file_document"));
        columns.add(Column.aliased("file_document_content_type", table, columnPrefix + "_file_document_content_type"));
        columns.add(Column.aliased("file_document_s_3_key", table, columnPrefix + "_file_document_s_3_key"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("last_modified_date", table, columnPrefix + "_last_modified_date"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("last_modified_by", table, columnPrefix + "_last_modified_by"));

        columns.add(Column.aliased("review_id", table, columnPrefix + "_review_id"));
        return columns;
    }
}
