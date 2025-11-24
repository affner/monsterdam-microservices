package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.HashTag;
import com.monsterdam.admin.domain.enumeration.HashtagType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link HashTag}, with proper type conversions.
 */
@Service
public class HashTagRowMapper implements BiFunction<Row, String, HashTag> {

    private final ColumnConverter converter;

    public HashTagRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HashTag} stored in the database.
     */
    @Override
    public HashTag apply(Row row, String prefix) {
        HashTag entity = new HashTag();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTagName(converter.fromRow(row, prefix + "_tag_name", String.class));
        entity.setHashtagType(converter.fromRow(row, prefix + "_hashtag_type", HashtagType.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
