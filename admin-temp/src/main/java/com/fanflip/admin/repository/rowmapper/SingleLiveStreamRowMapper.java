package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.SingleLiveStream;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SingleLiveStream}, with proper type conversions.
 */
@Service
public class SingleLiveStreamRowMapper implements BiFunction<Row, String, SingleLiveStream> {

    private final ColumnConverter converter;

    public SingleLiveStreamRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SingleLiveStream} stored in the database.
     */
    @Override
    public SingleLiveStream apply(Row row, String prefix) {
        SingleLiveStream entity = new SingleLiveStream();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setThumbnailContentType(converter.fromRow(row, prefix + "_thumbnail_content_type", String.class));
        entity.setThumbnail(converter.fromRow(row, prefix + "_thumbnail", byte[].class));
        entity.setThumbnailS3Key(converter.fromRow(row, prefix + "_thumbnail_s_3_key", String.class));
        entity.setStartTime(converter.fromRow(row, prefix + "_start_time", Instant.class));
        entity.setEndTime(converter.fromRow(row, prefix + "_end_time", Instant.class));
        entity.setLiveContentContentType(converter.fromRow(row, prefix + "_live_content_content_type", String.class));
        entity.setLiveContent(converter.fromRow(row, prefix + "_live_content", byte[].class));
        entity.setLiveContentS3Key(converter.fromRow(row, prefix + "_live_content_s_3_key", String.class));
        entity.setIsRecorded(converter.fromRow(row, prefix + "_is_recorded", Boolean.class));
        entity.setLikeCount(converter.fromRow(row, prefix + "_like_count", Long.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
