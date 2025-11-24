package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.Currency;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Currency}, with proper type conversions.
 */
@Service
public class CurrencyRowMapper implements BiFunction<Row, String, Currency> {

    private final ColumnConverter converter;

    public CurrencyRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Currency} stored in the database.
     */
    @Override
    public Currency apply(Row row, String prefix) {
        Currency entity = new Currency();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setSymbol(converter.fromRow(row, prefix + "_symbol", String.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
