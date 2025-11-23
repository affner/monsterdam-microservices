package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.PersonalSocialLinks;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PersonalSocialLinks}, with proper type conversions.
 */
@Service
public class PersonalSocialLinksRowMapper implements BiFunction<Row, String, PersonalSocialLinks> {

    private final ColumnConverter converter;

    public PersonalSocialLinksRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PersonalSocialLinks} stored in the database.
     */
    @Override
    public PersonalSocialLinks apply(Row row, String prefix) {
        PersonalSocialLinks entity = new PersonalSocialLinks();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setThumbnailContentType(converter.fromRow(row, prefix + "_thumbnail_content_type", String.class));
        entity.setThumbnail(converter.fromRow(row, prefix + "_thumbnail", byte[].class));
        entity.setNormalImageContentType(converter.fromRow(row, prefix + "_normal_image_content_type", String.class));
        entity.setNormalImage(converter.fromRow(row, prefix + "_normal_image", byte[].class));
        entity.setNormalImageS3Key(converter.fromRow(row, prefix + "_normal_image_s_3_key", String.class));
        entity.setThumbnailIconS3Key(converter.fromRow(row, prefix + "_thumbnail_icon_s_3_key", String.class));
        entity.setSocialLink(converter.fromRow(row, prefix + "_social_link", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setSocialNetworkId(converter.fromRow(row, prefix + "_social_network_id", Long.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
