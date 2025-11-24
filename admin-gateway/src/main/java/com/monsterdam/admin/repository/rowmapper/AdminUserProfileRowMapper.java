package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.AdminUserProfile;
import com.monsterdam.admin.domain.enumeration.AdminGender;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AdminUserProfile}, with proper type conversions.
 */
@Service
public class AdminUserProfileRowMapper implements BiFunction<Row, String, AdminUserProfile> {

    private final ColumnConverter converter;

    public AdminUserProfileRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AdminUserProfile} stored in the database.
     */
    @Override
    public AdminUserProfile apply(Row row, String prefix) {
        AdminUserProfile entity = new AdminUserProfile();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFullName(converter.fromRow(row, prefix + "_full_name", String.class));
        entity.setEmailAddress(converter.fromRow(row, prefix + "_email_address", String.class));
        entity.setNickName(converter.fromRow(row, prefix + "_nick_name", String.class));
        entity.setGender(converter.fromRow(row, prefix + "_gender", AdminGender.class));
        entity.setMobilePhone(converter.fromRow(row, prefix + "_mobile_phone", String.class));
        entity.setLastLoginDate(converter.fromRow(row, prefix + "_last_login_date", Instant.class));
        entity.setBirthDate(converter.fromRow(row, prefix + "_birth_date", LocalDate.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
