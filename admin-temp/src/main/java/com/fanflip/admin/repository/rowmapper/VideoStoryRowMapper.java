package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.VideoStory;
import io.r2dbc.spi.Row;
import java.time.Duration;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link VideoStory}, with proper type conversions.
 */
@Service
public class VideoStoryRowMapper implements BiFunction<Row, String, VideoStory> {

    private final ColumnConverter converter;

    public VideoStoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link VideoStory} stored in the database.
     */
    @Override
    public VideoStory apply(Row row, String prefix) {
        VideoStory entity = new VideoStory();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setThumbnailContentType(converter.fromRow(row, prefix + "_thumbnail_content_type", String.class));
        entity.setThumbnail(converter.fromRow(row, prefix + "_thumbnail", byte[].class));
        entity.setThumbnailS3Key(converter.fromRow(row, prefix + "_thumbnail_s_3_key", String.class));
        entity.setContentContentType(converter.fromRow(row, prefix + "_content_content_type", String.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", byte[].class));
        entity.setContentS3Key(converter.fromRow(row, prefix + "_content_s_3_key", String.class));
        entity.setDuration(converter.fromRow(row, prefix + "_duration", Duration.class));
        entity.setLikeCount(converter.fromRow(row, prefix + "_like_count", Long.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatorId(converter.fromRow(row, prefix + "_creator_id", Long.class));
        return entity;
    }
}
