package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.PurchasedTip;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PurchasedTip}, with proper type conversions.
 */
@Service
public class PurchasedTipRowMapper implements BiFunction<Row, String, PurchasedTip> {

    private final ColumnConverter converter;

    public PurchasedTipRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PurchasedTip} stored in the database.
     */
    @Override
    public PurchasedTip apply(Row row, String prefix) {
        PurchasedTip entity = new PurchasedTip();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAmount(converter.fromRow(row, prefix + "_amount", BigDecimal.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setPaymentId(converter.fromRow(row, prefix + "_payment_id", Long.class));
        entity.setWalletTransactionId(converter.fromRow(row, prefix + "_wallet_transaction_id", Long.class));
        entity.setCreatorEarningId(converter.fromRow(row, prefix + "_creator_earning_id", Long.class));
        entity.setMessageId(converter.fromRow(row, prefix + "_message_id", Long.class));
        return entity;
    }
}
