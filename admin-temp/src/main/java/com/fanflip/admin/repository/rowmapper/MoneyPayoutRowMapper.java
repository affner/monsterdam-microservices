package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.MoneyPayout;
import com.fanflip.admin.domain.enumeration.PayoutStatus;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link MoneyPayout}, with proper type conversions.
 */
@Service
public class MoneyPayoutRowMapper implements BiFunction<Row, String, MoneyPayout> {

    private final ColumnConverter converter;

    public MoneyPayoutRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link MoneyPayout} stored in the database.
     */
    @Override
    public MoneyPayout apply(Row row, String prefix) {
        MoneyPayout entity = new MoneyPayout();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAmount(converter.fromRow(row, prefix + "_amount", BigDecimal.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setWithdrawStatus(converter.fromRow(row, prefix + "_withdraw_status", PayoutStatus.class));
        entity.setCreatorEarningId(converter.fromRow(row, prefix + "_creator_earning_id", Long.class));
        entity.setCreatorId(converter.fromRow(row, prefix + "_creator_id", Long.class));
        return entity;
    }
}
