package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.PaymentTransaction;
import com.fanflip.admin.domain.enumeration.GenericStatus;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PaymentTransaction}, with proper type conversions.
 */
@Service
public class PaymentTransactionRowMapper implements BiFunction<Row, String, PaymentTransaction> {

    private final ColumnConverter converter;

    public PaymentTransactionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PaymentTransaction} stored in the database.
     */
    @Override
    public PaymentTransaction apply(Row row, String prefix) {
        PaymentTransaction entity = new PaymentTransaction();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAmount(converter.fromRow(row, prefix + "_amount", BigDecimal.class));
        entity.setPaymentDate(converter.fromRow(row, prefix + "_payment_date", Instant.class));
        entity.setPaymentStatus(converter.fromRow(row, prefix + "_payment_status", GenericStatus.class));
        entity.setPaymentReference(converter.fromRow(row, prefix + "_payment_reference", String.class));
        entity.setCloudTransactionId(converter.fromRow(row, prefix + "_cloud_transaction_id", String.class));
        entity.setPaymentMethodId(converter.fromRow(row, prefix + "_payment_method_id", Long.class));
        entity.setPaymentProviderId(converter.fromRow(row, prefix + "_payment_provider_id", Long.class));
        entity.setViewerId(converter.fromRow(row, prefix + "_viewer_id", Long.class));
        return entity;
    }
}
