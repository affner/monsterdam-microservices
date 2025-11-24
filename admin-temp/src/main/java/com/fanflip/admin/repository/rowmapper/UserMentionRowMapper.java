package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.UserMention;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserMention}, with proper type conversions.
 */
@Service
public class UserMentionRowMapper implements BiFunction<Row, String, UserMention> {

    private final ColumnConverter converter;

    public UserMentionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserMention} stored in the database.
     */
    @Override
    public UserMention apply(Row row, String prefix) {
        UserMention entity = new UserMention();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setOriginPostId(converter.fromRow(row, prefix + "_origin_post_id", Long.class));
        entity.setOriginPostCommentId(converter.fromRow(row, prefix + "_origin_post_comment_id", Long.class));
        entity.setMentionedUserId(converter.fromRow(row, prefix + "_mentioned_user_id", Long.class));
        return entity;
    }
}
