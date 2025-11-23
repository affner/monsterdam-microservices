package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.SubscriptionBundle;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SubscriptionBundle}, with proper type conversions.
 */
@Service
public class SubscriptionBundleRowMapper implements BiFunction<Row, String, SubscriptionBundle> {

    private final ColumnConverter converter;

    public SubscriptionBundleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SubscriptionBundle} stored in the database.
     */
    @Override
    public SubscriptionBundle apply(Row row, String prefix) {
        SubscriptionBundle entity = new SubscriptionBundle();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAmount(converter.fromRow(row, prefix + "_amount", BigDecimal.class));
        entity.setDuration(converter.fromRow(row, prefix + "_duration", Duration.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatorId(converter.fromRow(row, prefix + "_creator_id", Long.class));
        return entity;
    }
}
