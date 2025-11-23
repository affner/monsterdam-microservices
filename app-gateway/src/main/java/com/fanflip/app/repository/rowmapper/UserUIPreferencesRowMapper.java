package com.fanflip.app.repository.rowmapper;

import com.fanflip.app.domain.UserUIPreferences;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserUIPreferences}, with proper type conversions.
 */
@Service
public class UserUIPreferencesRowMapper implements BiFunction<Row, String, UserUIPreferences> {

    private final ColumnConverter converter;

    public UserUIPreferencesRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserUIPreferences} stored in the database.
     */
    @Override
    public UserUIPreferences apply(Row row, String prefix) {
        UserUIPreferences entity = new UserUIPreferences();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPreferences(converter.fromRow(row, prefix + "_preferences", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        return entity;
    }
}
