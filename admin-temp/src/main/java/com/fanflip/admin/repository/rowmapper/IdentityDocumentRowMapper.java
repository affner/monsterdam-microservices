package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.IdentityDocument;
import com.fanflip.admin.domain.enumeration.DocumentStatus;
import com.fanflip.admin.domain.enumeration.DocumentType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IdentityDocument}, with proper type conversions.
 */
@Service
public class IdentityDocumentRowMapper implements BiFunction<Row, String, IdentityDocument> {

    private final ColumnConverter converter;

    public IdentityDocumentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IdentityDocument} stored in the database.
     */
    @Override
    public IdentityDocument apply(Row row, String prefix) {
        IdentityDocument entity = new IdentityDocument();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDocumentName(converter.fromRow(row, prefix + "_document_name", String.class));
        entity.setDocumentDescription(converter.fromRow(row, prefix + "_document_description", String.class));
        entity.setDocumentStatus(converter.fromRow(row, prefix + "_document_status", DocumentStatus.class));
        entity.setDocumentType(converter.fromRow(row, prefix + "_document_type", DocumentType.class));
        entity.setFileDocumentContentType(converter.fromRow(row, prefix + "_file_document_content_type", String.class));
        entity.setFileDocument(converter.fromRow(row, prefix + "_file_document", byte[].class));
        entity.setFileDocumentS3Key(converter.fromRow(row, prefix + "_file_document_s_3_key", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setReviewId(converter.fromRow(row, prefix + "_review_id", Long.class));
        return entity;
    }
}
