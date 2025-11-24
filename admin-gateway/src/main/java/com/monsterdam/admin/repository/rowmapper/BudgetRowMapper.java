package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.Budget;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Budget}, with proper type conversions.
 */
@Service
public class BudgetRowMapper implements BiFunction<Row, String, Budget> {

    private final ColumnConverter converter;

    public BudgetRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Budget} stored in the database.
     */
    @Override
    public Budget apply(Row row, String prefix) {
        Budget entity = new Budget();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setYear(converter.fromRow(row, prefix + "_year", Integer.class));
        entity.setTotalBudget(converter.fromRow(row, prefix + "_total_budget", BigDecimal.class));
        entity.setSpentAmount(converter.fromRow(row, prefix + "_spent_amount", BigDecimal.class));
        entity.setRemainingAmount(converter.fromRow(row, prefix + "_remaining_amount", BigDecimal.class));
        entity.setBudgetDetails(converter.fromRow(row, prefix + "_budget_details", String.class));
        return entity;
    }
}
