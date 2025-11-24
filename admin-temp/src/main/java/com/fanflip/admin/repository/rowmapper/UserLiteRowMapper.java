package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.UserLite;
import com.monsterdam.admin.domain.enumeration.ContentPreference;
import com.monsterdam.admin.domain.enumeration.UserGender;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserLite}, with proper type conversions.
 */
@Service
public class UserLiteRowMapper implements BiFunction<Row, String, UserLite> {

    private final ColumnConverter converter;

    public UserLiteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserLite} stored in the database.
     */
    @Override
    public UserLite apply(Row row, String prefix) {
        UserLite entity = new UserLite();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setThumbnailContentType(converter.fromRow(row, prefix + "_thumbnail_content_type", String.class));
        entity.setThumbnail(converter.fromRow(row, prefix + "_thumbnail", byte[].class));
        entity.setThumbnailS3Key(converter.fromRow(row, prefix + "_thumbnail_s_3_key", String.class));
        entity.setBirthDate(converter.fromRow(row, prefix + "_birth_date", LocalDate.class));
        entity.setGender(converter.fromRow(row, prefix + "_gender", UserGender.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setNickName(converter.fromRow(row, prefix + "_nick_name", String.class));
        entity.setFullName(converter.fromRow(row, prefix + "_full_name", String.class));
        entity.setContentPreference(converter.fromRow(row, prefix + "_content_preference", ContentPreference.class));
        return entity;
    }
}
