package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.ChatRoom;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ChatRoom}, with proper type conversions.
 */
@Service
public class ChatRoomRowMapper implements BiFunction<Row, String, ChatRoom> {

    private final ColumnConverter converter;

    public ChatRoomRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ChatRoom} stored in the database.
     */
    @Override
    public ChatRoom apply(Row row, String prefix) {
        ChatRoom entity = new ChatRoom();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLastAction(converter.fromRow(row, prefix + "_last_action", String.class));
        entity.setLastConnectionDate(converter.fromRow(row, prefix + "_last_connection_date", Instant.class));
        entity.setMuted(converter.fromRow(row, prefix + "_muted", Boolean.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
