package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.AdminAnnouncement;
import com.monsterdam.admin.domain.enumeration.AdminAnnouncementType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AdminAnnouncement}, with proper type conversions.
 */
@Service
public class AdminAnnouncementRowMapper implements BiFunction<Row, String, AdminAnnouncement> {

    private final ColumnConverter converter;

    public AdminAnnouncementRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AdminAnnouncement} stored in the database.
     */
    @Override
    public AdminAnnouncement apply(Row row, String prefix) {
        AdminAnnouncement entity = new AdminAnnouncement();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAnnouncementType(converter.fromRow(row, prefix + "_announcement_type", AdminAnnouncementType.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setAnnouncerMessageId(converter.fromRow(row, prefix + "_announcer_message_id", Long.class));
        entity.setAdminId(converter.fromRow(row, prefix + "_admin_id", Long.class));
        return entity;
    }
}
