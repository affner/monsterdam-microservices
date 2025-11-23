package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.BookMark;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link BookMark}, with proper type conversions.
 */
@Service
public class BookMarkRowMapper implements BiFunction<Row, String, BookMark> {

    private final ColumnConverter converter;

    public BookMarkRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link BookMark} stored in the database.
     */
    @Override
    public BookMark apply(Row row, String prefix) {
        BookMark entity = new BookMark();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setPostId(converter.fromRow(row, prefix + "_post_id", Long.class));
        entity.setMessageId(converter.fromRow(row, prefix + "_message_id", Long.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
