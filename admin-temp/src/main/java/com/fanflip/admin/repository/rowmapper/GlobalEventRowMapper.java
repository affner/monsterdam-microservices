package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.GlobalEvent;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link GlobalEvent}, with proper type conversions.
 */
@Service
public class GlobalEventRowMapper implements BiFunction<Row, String, GlobalEvent> {

    private final ColumnConverter converter;

    public GlobalEventRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link GlobalEvent} stored in the database.
     */
    @Override
    public GlobalEvent apply(Row row, String prefix) {
        GlobalEvent entity = new GlobalEvent();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setEventName(converter.fromRow(row, prefix + "_event_name", String.class));
        entity.setStartDate(converter.fromRow(row, prefix + "_start_date", LocalDate.class));
        entity.setEndDate(converter.fromRow(row, prefix + "_end_date", LocalDate.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
