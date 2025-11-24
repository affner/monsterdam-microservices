package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.State;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link State}, with proper type conversions.
 */
@Service
public class StateRowMapper implements BiFunction<Row, String, State> {

    private final ColumnConverter converter;

    public StateRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link State} stored in the database.
     */
    @Override
    public State apply(Row row, String prefix) {
        State entity = new State();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setStateName(converter.fromRow(row, prefix + "_state_name", String.class));
        entity.setIsoCode(converter.fromRow(row, prefix + "_iso_code", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCountryId(converter.fromRow(row, prefix + "_country_id", Long.class));
        return entity;
    }
}
