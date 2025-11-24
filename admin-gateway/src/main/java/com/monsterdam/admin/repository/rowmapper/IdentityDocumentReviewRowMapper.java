package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.IdentityDocumentReview;
import com.monsterdam.admin.domain.enumeration.DocumentStatus;
import com.monsterdam.admin.domain.enumeration.ReviewStatus;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IdentityDocumentReview}, with proper type conversions.
 */
@Service
public class IdentityDocumentReviewRowMapper implements BiFunction<Row, String, IdentityDocumentReview> {

    private final ColumnConverter converter;

    public IdentityDocumentReviewRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IdentityDocumentReview} stored in the database.
     */
    @Override
    public IdentityDocumentReview apply(Row row, String prefix) {
        IdentityDocumentReview entity = new IdentityDocumentReview();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDocumentStatus(converter.fromRow(row, prefix + "_document_status", DocumentStatus.class));
        entity.setResolutionDate(converter.fromRow(row, prefix + "_resolution_date", Instant.class));
        entity.setReviewStatus(converter.fromRow(row, prefix + "_review_status", ReviewStatus.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setTicketId(converter.fromRow(row, prefix + "_ticket_id", Long.class));
        return entity;
    }
}
