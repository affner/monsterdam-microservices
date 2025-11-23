package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.ContentPackage;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ContentPackage}, with proper type conversions.
 */
@Service
public class ContentPackageRowMapper implements BiFunction<Row, String, ContentPackage> {

    private final ColumnConverter converter;

    public ContentPackageRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ContentPackage} stored in the database.
     */
    @Override
    public ContentPackage apply(Row row, String prefix) {
        ContentPackage entity = new ContentPackage();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAmount(converter.fromRow(row, prefix + "_amount", BigDecimal.class));
        entity.setVideoCount(converter.fromRow(row, prefix + "_video_count", Integer.class));
        entity.setImageCount(converter.fromRow(row, prefix + "_image_count", Integer.class));
        entity.setIsPaidContent(converter.fromRow(row, prefix + "_is_paid_content", Boolean.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setAudioId(converter.fromRow(row, prefix + "_audio_id", Long.class));
        return entity;
    }
}
