package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.Liability;
import com.monsterdam.admin.domain.enumeration.LiabilityType;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Liability}, with proper type conversions.
 */
@Service
public class LiabilityRowMapper implements BiFunction<Row, String, Liability> {

    private final ColumnConverter converter;

    public LiabilityRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Liability} stored in the database.
     */
    @Override
    public Liability apply(Row row, String prefix) {
        Liability entity = new Liability();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setAmount(converter.fromRow(row, prefix + "_amount", BigDecimal.class));
        entity.setDueDate(converter.fromRow(row, prefix + "_due_date", LocalDate.class));
        entity.setType(converter.fromRow(row, prefix + "_type", LiabilityType.class));
        return entity;
    }
}
