package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.SpecialAward;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SpecialAward}, with proper type conversions.
 */
@Service
public class SpecialAwardRowMapper implements BiFunction<Row, String, SpecialAward> {

    private final ColumnConverter converter;

    public SpecialAwardRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SpecialAward} stored in the database.
     */
    @Override
    public SpecialAward apply(Row row, String prefix) {
        SpecialAward entity = new SpecialAward();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setStartDate(converter.fromRow(row, prefix + "_start_date", LocalDate.class));
        entity.setEndDate(converter.fromRow(row, prefix + "_end_date", LocalDate.class));
        entity.setReason(converter.fromRow(row, prefix + "_reason", String.class));
        entity.setAltSpecialTitle(converter.fromRow(row, prefix + "_alt_special_title", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setViewerId(converter.fromRow(row, prefix + "_viewer_id", Long.class));
        entity.setCreatorId(converter.fromRow(row, prefix + "_creator_id", Long.class));
        entity.setSpecialTitleId(converter.fromRow(row, prefix + "_special_title_id", Long.class));
        return entity;
    }
}
