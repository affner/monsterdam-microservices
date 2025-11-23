package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.UserReport;
import com.fanflip.admin.domain.enumeration.ReportCategory;
import com.fanflip.admin.domain.enumeration.ReportStatus;
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
        entity.setReporterId(converter.fromRow(row, prefix + "_reporter_id", Long.class));
        entity.setReportedId(converter.fromRow(row, prefix + "_reported_id", Long.class));
        entity.setMultimediaId(converter.fromRow(row, prefix + "_multimedia_id", Long.class));
        entity.setMessageId(converter.fromRow(row, prefix + "_message_id", Long.class));
        entity.setPostId(converter.fromRow(row, prefix + "_post_id", Long.class));
        entity.setCommentId(converter.fromRow(row, prefix + "_comment_id", Long.class));
        entity.setTicketId(converter.fromRow(row, prefix + "_ticket_id", Long.class));
        return entity;
    }
}
