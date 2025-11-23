package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.PurchasedContent;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PurchasedContent}, with proper type conversions.
 */
@Service
public class PurchasedContentRowMapper implements BiFunction<Row, String, PurchasedContent> {

    private final ColumnConverter converter;

    public PurchasedContentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PurchasedContent} stored in the database.
     */
    @Override
    public PurchasedContent apply(Row row, String prefix) {
        PurchasedContent entity = new PurchasedContent();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setRating(converter.fromRow(row, prefix + "_rating", Float.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setPaymentId(converter.fromRow(row, prefix + "_payment_id", Long.class));
        entity.setWalletTransactionId(converter.fromRow(row, prefix + "_wallet_transaction_id", Long.class));
        entity.setCreatorEarningId(converter.fromRow(row, prefix + "_creator_earning_id", Long.class));
        entity.setViewerId(converter.fromRow(row, prefix + "_viewer_id", Long.class));
        entity.setPurchasedContentPackageId(converter.fromRow(row, prefix + "_purchased_content_package_id", Long.class));
        return entity;
    }
}
