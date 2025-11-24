package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.EmojiType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link EmojiType}, with proper type conversions.
 */
@Service
public class EmojiTypeRowMapper implements BiFunction<Row, String, EmojiType> {

    private final ColumnConverter converter;

    public EmojiTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link EmojiType} stored in the database.
     */
    @Override
    public EmojiType apply(Row row, String prefix) {
        EmojiType entity = new EmojiType();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setThumbnailContentType(converter.fromRow(row, prefix + "_thumbnail_content_type", String.class));
        entity.setThumbnail(converter.fromRow(row, prefix + "_thumbnail", byte[].class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
