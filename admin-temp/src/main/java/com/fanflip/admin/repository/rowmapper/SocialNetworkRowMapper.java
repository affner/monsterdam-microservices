package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.SocialNetwork;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SocialNetwork}, with proper type conversions.
 */
@Service
public class SocialNetworkRowMapper implements BiFunction<Row, String, SocialNetwork> {

    private final ColumnConverter converter;

    public SocialNetworkRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SocialNetwork} stored in the database.
     */
    @Override
    public SocialNetwork apply(Row row, String prefix) {
        SocialNetwork entity = new SocialNetwork();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setThumbnailContentType(converter.fromRow(row, prefix + "_thumbnail_content_type", String.class));
        entity.setThumbnail(converter.fromRow(row, prefix + "_thumbnail", byte[].class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setCompleteName(converter.fromRow(row, prefix + "_complete_name", String.class));
        entity.setMainLink(converter.fromRow(row, prefix + "_main_link", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
