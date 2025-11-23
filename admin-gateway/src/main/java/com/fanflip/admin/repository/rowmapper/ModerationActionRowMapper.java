package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.ModerationAction;
import com.fanflip.admin.domain.enumeration.ModerationActionType;
import io.r2dbc.spi.Row;
import java.time.Duration;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ModerationAction}, with proper type conversions.
 */
@Service
public class ModerationActionRowMapper implements BiFunction<Row, String, ModerationAction> {

    private final ColumnConverter converter;

    public ModerationActionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ModerationAction} stored in the database.
     */
    @Override
    public ModerationAction apply(Row row, String prefix) {
        ModerationAction entity = new ModerationAction();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setActionType(converter.fromRow(row, prefix + "_action_type", ModerationActionType.class));
        entity.setReason(converter.fromRow(row, prefix + "_reason", String.class));
        entity.setActionDate(converter.fromRow(row, prefix + "_action_date", Instant.class));
        entity.setDurationDays(converter.fromRow(row, prefix + "_duration_days", Duration.class));
        return entity;
    }
}
