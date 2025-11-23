package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.PostComment;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PostComment}, with proper type conversions.
 */
@Service
public class PostCommentRowMapper implements BiFunction<Row, String, PostComment> {

    private final ColumnConverter converter;

    public PostCommentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PostComment} stored in the database.
     */
    @Override
    public PostComment apply(Row row, String prefix) {
        PostComment entity = new PostComment();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCommentContent(converter.fromRow(row, prefix + "_comment_content", String.class));
        entity.setLikeCount(converter.fromRow(row, prefix + "_like_count", Long.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setPostId(converter.fromRow(row, prefix + "_post_id", Long.class));
        entity.setResponseToId(converter.fromRow(row, prefix + "_response_to_id", Long.class));
        entity.setCommenterId(converter.fromRow(row, prefix + "_commenter_id", Long.class));
        return entity;
    }
}
