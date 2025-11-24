package com.monsterdam.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TaxDeclarationSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("year", table, columnPrefix + "_year"));
        columns.add(Column.aliased("declaration_type", table, columnPrefix + "_declaration_type"));
        columns.add(Column.aliased("submitted_date", table, columnPrefix + "_submitted_date"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("total_income", table, columnPrefix + "_total_income"));
        columns.add(Column.aliased("total_taxable_income", table, columnPrefix + "_total_taxable_income"));
        columns.add(Column.aliased("total_tax_paid", table, columnPrefix + "_total_tax_paid"));
        columns.add(Column.aliased("supporting_documents_key", table, columnPrefix + "_supporting_documents_key"));

        return columns;
    }
}
