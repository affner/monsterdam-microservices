package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.PostFeed;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PostFeed}, with proper type conversions.
 */
@Service
public class PostFeedRowMapper implements BiFunction<Row, String, PostFeed> {

    private final ColumnConverter converter;

    public PostFeedRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PostFeed} stored in the database.
     */
    @Override
    public PostFeed apply(Row row, String prefix) {
        PostFeed entity = new PostFeed();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPostContent(converter.fromRow(row, prefix + "_post_content", String.class));
        entity.setIsHidden(converter.fromRow(row, prefix + "_is_hidden", Boolean.class));
        entity.setPinnedPost(converter.fromRow(row, prefix + "_pinned_post", Boolean.class));
        entity.setLikeCount(converter.fromRow(row, prefix + "_like_count", Long.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setPollId(converter.fromRow(row, prefix + "_poll_id", Long.class));
        entity.setContentPackageId(converter.fromRow(row, prefix + "_content_package_id", Long.class));
        entity.setCreatorId(converter.fromRow(row, prefix + "_creator_id", Long.class));
        return entity;
    }
}
