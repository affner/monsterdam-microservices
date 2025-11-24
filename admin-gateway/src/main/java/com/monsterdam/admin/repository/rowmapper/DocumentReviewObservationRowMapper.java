package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.DocumentReviewObservation;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DocumentReviewObservation}, with proper type conversions.
 */
@Service
public class DocumentReviewObservationRowMapper implements BiFunction<Row, String, DocumentReviewObservation> {

    private final ColumnConverter converter;

    public DocumentReviewObservationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DocumentReviewObservation} stored in the database.
     */
    @Override
    public DocumentReviewObservation apply(Row row, String prefix) {
        DocumentReviewObservation entity = new DocumentReviewObservation();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCommentDate(converter.fromRow(row, prefix + "_comment_date", Instant.class));
        entity.setComment(converter.fromRow(row, prefix + "_comment", String.class));
        entity.setReviewId(converter.fromRow(row, prefix + "_review_id", Long.class));
        return entity;
    }
}
