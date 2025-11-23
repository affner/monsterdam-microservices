package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.PurchasedSubscription;
import com.fanflip.admin.domain.enumeration.PurchasedSubscriptionStatus;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PurchasedSubscription}, with proper type conversions.
 */
@Service
public class PurchasedSubscriptionRowMapper implements BiFunction<Row, String, PurchasedSubscription> {

    private final ColumnConverter converter;

    public PurchasedSubscriptionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PurchasedSubscription} stored in the database.
     */
    @Override
    public PurchasedSubscription apply(Row row, String prefix) {
        PurchasedSubscription entity = new PurchasedSubscription();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setEndDate(converter.fromRow(row, prefix + "_end_date", LocalDate.class));
        entity.setSubscriptionStatus(converter.fromRow(row, prefix + "_subscription_status", PurchasedSubscriptionStatus.class));
        entity.setViewerId(converter.fromRow(row, prefix + "_viewer_id", Long.class));
        entity.setCreatorId(converter.fromRow(row, prefix + "_creator_id", Long.class));
        entity.setPaymentId(converter.fromRow(row, prefix + "_payment_id", Long.class));
        entity.setWalletTransactionId(converter.fromRow(row, prefix + "_wallet_transaction_id", Long.class));
        entity.setCreatorEarningId(converter.fromRow(row, prefix + "_creator_earning_id", Long.class));
        entity.setSubscriptionBundleId(converter.fromRow(row, prefix + "_subscription_bundle_id", Long.class));
        entity.setAppliedPromotionId(converter.fromRow(row, prefix + "_applied_promotion_id", Long.class));
        entity.setViewerId(converter.fromRow(row, prefix + "_viewer_id", Long.class));
        return entity;
    }
}
