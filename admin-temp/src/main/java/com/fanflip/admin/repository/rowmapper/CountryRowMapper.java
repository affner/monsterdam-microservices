package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.Country;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Country}, with proper type conversions.
 */
@Service
public class CountryRowMapper implements BiFunction<Row, String, Country> {

    private final ColumnConverter converter;

    public CountryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Country} stored in the database.
     */
    @Override
    public Country apply(Row row, String prefix) {
        Country entity = new Country();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setAlpha2Code(converter.fromRow(row, prefix + "_alpha_2_code", String.class));
        entity.setAlpha3Code(converter.fromRow(row, prefix + "_alpha_3_code", String.class));
        entity.setPhoneCode(converter.fromRow(row, prefix + "_phone_code", String.class));
        entity.setThumbnailCountryContentType(converter.fromRow(row, prefix + "_thumbnail_country_content_type", String.class));
        entity.setThumbnailCountry(converter.fromRow(row, prefix + "_thumbnail_country", byte[].class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
