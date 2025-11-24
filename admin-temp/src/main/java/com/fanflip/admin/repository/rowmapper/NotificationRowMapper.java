package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.Notification;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Notification}, with proper type conversions.
 */
@Service
public class NotificationRowMapper implements BiFunction<Row, String, Notification> {

    private final ColumnConverter converter;

    public NotificationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Notification} stored in the database.
     */
    @Override
    public Notification apply(Row row, String prefix) {
        Notification entity = new Notification();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setReadDate(converter.fromRow(row, prefix + "_read_date", Instant.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setPostCommentId(converter.fromRow(row, prefix + "_post_comment_id", Long.class));
        entity.setPostFeedId(converter.fromRow(row, prefix + "_post_feed_id", Long.class));
        entity.setDirectMessageId(converter.fromRow(row, prefix + "_direct_message_id", Long.class));
        entity.setUserMentionId(converter.fromRow(row, prefix + "_user_mention_id", Long.class));
        entity.setLikeMarkId(converter.fromRow(row, prefix + "_like_mark_id", Long.class));
        entity.setCommentedUserId(converter.fromRow(row, prefix + "_commented_user_id", Long.class));
        entity.setMessagedUserId(converter.fromRow(row, prefix + "_messaged_user_id", Long.class));
        entity.setMentionerUserInPostId(converter.fromRow(row, prefix + "_mentioner_user_in_post_id", Long.class));
        entity.setMentionerUserInCommentId(converter.fromRow(row, prefix + "_mentioner_user_in_comment_id", Long.class));
        return entity;
    }
}
