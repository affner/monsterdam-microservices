package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.FinancialStatement;
import com.monsterdam.admin.domain.enumeration.StatementType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FinancialStatement}, with proper type conversions.
 */
@Service
public class FinancialStatementRowMapper implements BiFunction<Row, String, FinancialStatement> {

    private final ColumnConverter converter;

    public FinancialStatementRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FinancialStatement} stored in the database.
     */
    @Override
    public FinancialStatement apply(Row row, String prefix) {
        FinancialStatement entity = new FinancialStatement();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setStatementType(converter.fromRow(row, prefix + "_statement_type", StatementType.class));
        entity.setPeriodStartDate(converter.fromRow(row, prefix + "_period_start_date", LocalDate.class));
        entity.setPeriodEndDate(converter.fromRow(row, prefix + "_period_end_date", LocalDate.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        return entity;
    }
}
