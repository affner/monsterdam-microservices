package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.UserAssociation;
import com.fanflip.admin.domain.enumeration.AssociationStatus;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserAssociation}, with proper type conversions.
 */
@Service
public class UserAssociationRowMapper implements BiFunction<Row, String, UserAssociation> {

    private final ColumnConverter converter;

    public UserAssociationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserAssociation} stored in the database.
     */
    @Override
    public UserAssociation apply(Row row, String prefix) {
        UserAssociation entity = new UserAssociation();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setRequestedDate(converter.fromRow(row, prefix + "_requested_date", Instant.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", AssociationStatus.class));
        entity.setAssociationToken(converter.fromRow(row, prefix + "_association_token", String.class));
        entity.setExpiryDate(converter.fromRow(row, prefix + "_expiry_date", Instant.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setOwnerId(converter.fromRow(row, prefix + "_owner_id", Long.class));
        return entity;
    }
}
