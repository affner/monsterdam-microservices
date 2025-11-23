package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.DirectMessage;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DirectMessage}, with proper type conversions.
 */
@Service
public class DirectMessageRowMapper implements BiFunction<Row, String, DirectMessage> {

    private final ColumnConverter converter;

    public DirectMessageRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DirectMessage} stored in the database.
     */
    @Override
    public DirectMessage apply(Row row, String prefix) {
        DirectMessage entity = new DirectMessage();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setMessageContent(converter.fromRow(row, prefix + "_message_content", String.class));
        entity.setReadDate(converter.fromRow(row, prefix + "_read_date", Instant.class));
        entity.setLikeCount(converter.fromRow(row, prefix + "_like_count", Long.class));
        entity.setIsHidden(converter.fromRow(row, prefix + "_is_hidden", Boolean.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setContentPackageId(converter.fromRow(row, prefix + "_content_package_id", Long.class));
        entity.setResponseToId(converter.fromRow(row, prefix + "_response_to_id", Long.class));
        entity.setRepliedStoryId(converter.fromRow(row, prefix + "_replied_story_id", Long.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
