package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.WalletTransaction;
import com.fanflip.admin.domain.enumeration.WalletTransactionType;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link WalletTransaction}, with proper type conversions.
 */
@Service
public class WalletTransactionRowMapper implements BiFunction<Row, String, WalletTransaction> {

    private final ColumnConverter converter;

    public WalletTransactionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link WalletTransaction} stored in the database.
     */
    @Override
    public WalletTransaction apply(Row row, String prefix) {
        WalletTransaction entity = new WalletTransaction();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAmount(converter.fromRow(row, prefix + "_amount", BigDecimal.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setTransactionType(converter.fromRow(row, prefix + "_transaction_type", WalletTransactionType.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setPaymentId(converter.fromRow(row, prefix + "_payment_id", Long.class));
        entity.setViewerId(converter.fromRow(row, prefix + "_viewer_id", Long.class));
        return entity;
    }
}
