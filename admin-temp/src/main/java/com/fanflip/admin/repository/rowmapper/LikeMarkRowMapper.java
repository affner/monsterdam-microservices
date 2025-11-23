package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.LikeMark;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link LikeMark}, with proper type conversions.
 */
@Service
public class LikeMarkRowMapper implements BiFunction<Row, String, LikeMark> {

    private final ColumnConverter converter;

    public LikeMarkRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link LikeMark} stored in the database.
     */
    @Override
    public LikeMark apply(Row row, String prefix) {
        LikeMark entity = new LikeMark();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setEmojiTypeId(converter.fromRow(row, prefix + "_emoji_type_id", Long.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setMultimediaId(converter.fromRow(row, prefix + "_multimedia_id", Long.class));
        entity.setMessageId(converter.fromRow(row, prefix + "_message_id", Long.class));
        entity.setPostId(converter.fromRow(row, prefix + "_post_id", Long.class));
        entity.setCommentId(converter.fromRow(row, prefix + "_comment_id", Long.class));
        entity.setLikerUserId(converter.fromRow(row, prefix + "_liker_user_id", Long.class));
        return entity;
    }
}
