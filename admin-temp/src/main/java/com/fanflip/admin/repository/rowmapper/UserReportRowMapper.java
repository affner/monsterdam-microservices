package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.UserReport;
import com.monsterdam.admin.domain.enumeration.ReportCategory;
import com.monsterdam.admin.domain.enumeration.ReportStatus;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserReport}, with proper type conversions.
 */
@Service
public class UserReportRowMapper implements BiFunction<Row, String, UserReport> {

    private final ColumnConverter converter;

    public UserReportRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserReport} stored in the database.
     */
    @Override
    public UserReport apply(Row row, String prefix) {
        UserReport entity = new UserReport();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setReportDescription(converter.fromRow(row, prefix + "_report_description", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", ReportStatus.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setReportCategory(converter.fromRow(row, prefix + "_report_category", ReportCategory.class));
        entity.setTicketId(converter.fromRow(row, prefix + "_ticket_id", Long.class));
        entity.setReporterId(converter.fromRow(row, prefix + "_reporter_id", Long.class));
        entity.setReportedId(converter.fromRow(row, prefix + "_reported_id", Long.class));
        entity.setStoryId(converter.fromRow(row, prefix + "_story_id", Long.class));
        entity.setVideoId(converter.fromRow(row, prefix + "_video_id", Long.class));
        entity.setPhotoId(converter.fromRow(row, prefix + "_photo_id", Long.class));
        entity.setAudioId(converter.fromRow(row, prefix + "_audio_id", Long.class));
        entity.setLiveStreamId(converter.fromRow(row, prefix + "_live_stream_id", Long.class));
        entity.setMessageId(converter.fromRow(row, prefix + "_message_id", Long.class));
        entity.setPostId(converter.fromRow(row, prefix + "_post_id", Long.class));
        entity.setPostCommentId(converter.fromRow(row, prefix + "_post_comment_id", Long.class));
        return entity;
    }
}
