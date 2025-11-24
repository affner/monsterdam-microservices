package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.AssistanceTicket;
import com.monsterdam.admin.domain.enumeration.TicketStatus;
import com.monsterdam.admin.domain.enumeration.TicketType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AssistanceTicket}, with proper type conversions.
 */
@Service
public class AssistanceTicketRowMapper implements BiFunction<Row, String, AssistanceTicket> {

    private final ColumnConverter converter;

    public AssistanceTicketRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AssistanceTicket} stored in the database.
     */
    @Override
    public AssistanceTicket apply(Row row, String prefix) {
        AssistanceTicket entity = new AssistanceTicket();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSubject(converter.fromRow(row, prefix + "_subject", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", TicketStatus.class));
        entity.setType(converter.fromRow(row, prefix + "_type", TicketType.class));
        entity.setOpenedAt(converter.fromRow(row, prefix + "_opened_at", Instant.class));
        entity.setClosedAt(converter.fromRow(row, prefix + "_closed_at", Instant.class));
        entity.setComments(converter.fromRow(row, prefix + "_comments", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        entity.setModerationActionId(converter.fromRow(row, prefix + "_moderation_action_id", Long.class));
        entity.setAssignedAdminId(converter.fromRow(row, prefix + "_assigned_admin_id", Long.class));
        return entity;
    }
}
