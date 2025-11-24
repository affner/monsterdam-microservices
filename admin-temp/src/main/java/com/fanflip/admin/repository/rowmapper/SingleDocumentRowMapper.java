package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.SingleDocument;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SingleDocument}, with proper type conversions.
 */
@Service
public class SingleDocumentRowMapper implements BiFunction<Row, String, SingleDocument> {

    private final ColumnConverter converter;

    public SingleDocumentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SingleDocument} stored in the database.
     */
    @Override
    public SingleDocument apply(Row row, String prefix) {
        SingleDocument entity = new SingleDocument();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setDocumentFileContentType(converter.fromRow(row, prefix + "_document_file_content_type", String.class));
        entity.setDocumentFile(converter.fromRow(row, prefix + "_document_file", byte[].class));
        entity.setDocumentFileS3Key(converter.fromRow(row, prefix + "_document_file_s_3_key", String.class));
        entity.setDocumentType(converter.fromRow(row, prefix + "_document_type", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
