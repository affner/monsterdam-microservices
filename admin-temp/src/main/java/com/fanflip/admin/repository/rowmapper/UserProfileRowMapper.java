package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.UserProfile;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserProfile}, with proper type conversions.
 */
@Service
public class UserProfileRowMapper implements BiFunction<Row, String, UserProfile> {

    private final ColumnConverter converter;

    public UserProfileRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserProfile} stored in the database.
     */
    @Override
    public UserProfile apply(Row row, String prefix) {
        UserProfile entity = new UserProfile();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setEmailContact(converter.fromRow(row, prefix + "_email_contact", String.class));
        entity.setProfilePhotoContentType(converter.fromRow(row, prefix + "_profile_photo_content_type", String.class));
        entity.setProfilePhoto(converter.fromRow(row, prefix + "_profile_photo", byte[].class));
        entity.setCoverPhotoContentType(converter.fromRow(row, prefix + "_cover_photo_content_type", String.class));
        entity.setCoverPhoto(converter.fromRow(row, prefix + "_cover_photo", byte[].class));
        entity.setProfilePhotoS3Key(converter.fromRow(row, prefix + "_profile_photo_s_3_key", String.class));
        entity.setCoverPhotoS3Key(converter.fromRow(row, prefix + "_cover_photo_s_3_key", String.class));
        entity.setMainContentUrl(converter.fromRow(row, prefix + "_main_content_url", String.class));
        entity.setMobilePhone(converter.fromRow(row, prefix + "_mobile_phone", String.class));
        entity.setWebsiteUrl(converter.fromRow(row, prefix + "_website_url", String.class));
        entity.setAmazonWishlistUrl(converter.fromRow(row, prefix + "_amazon_wishlist_url", String.class));
        entity.setLastLoginDate(converter.fromRow(row, prefix + "_last_login_date", Instant.class));
        entity.setBiography(converter.fromRow(row, prefix + "_biography", String.class));
        entity.setIsFree(converter.fromRow(row, prefix + "_is_free", Boolean.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setUserLiteId(converter.fromRow(row, prefix + "_user_lite_id", Long.class));
        entity.setSettingsId(converter.fromRow(row, prefix + "_settings_id", Long.class));
        entity.setCountryOfBirthId(converter.fromRow(row, prefix + "_country_of_birth_id", Long.class));
        entity.setStateOfResidenceId(converter.fromRow(row, prefix + "_state_of_residence_id", Long.class));
        return entity;
    }
}
