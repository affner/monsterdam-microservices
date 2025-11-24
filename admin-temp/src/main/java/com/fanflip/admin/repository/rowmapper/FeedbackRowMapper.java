package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.Feedback;
import com.monsterdam.admin.domain.enumeration.FeedbackType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Feedback}, with proper type conversions.
 */
@Service
public class FeedbackRowMapper implements BiFunction<Row, String, Feedback> {

    private final ColumnConverter converter;

    public FeedbackRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Feedback} stored in the database.
     */
    @Override
    public Feedback apply(Row row, String prefix) {
        Feedback entity = new Feedback();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setFeedbackDate(converter.fromRow(row, prefix + "_feedback_date", Instant.class));
        entity.setFeedbackRating(converter.fromRow(row, prefix + "_feedback_rating", Integer.class));
        entity.setFeedbackType(converter.fromRow(row, prefix + "_feedback_type", FeedbackType.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatorId(converter.fromRow(row, prefix + "_creator_id", Long.class));
        return entity;
    }
}
