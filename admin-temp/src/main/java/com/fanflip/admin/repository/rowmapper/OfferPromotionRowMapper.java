package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.OfferPromotion;
import com.fanflip.admin.domain.enumeration.OfferPromotionType;
import io.r2dbc.spi.Row;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link OfferPromotion}, with proper type conversions.
 */
@Service
public class OfferPromotionRowMapper implements BiFunction<Row, String, OfferPromotion> {

    private final ColumnConverter converter;

    public OfferPromotionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link OfferPromotion} stored in the database.
     */
    @Override
    public OfferPromotion apply(Row row, String prefix) {
        OfferPromotion entity = new OfferPromotion();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFreeDaysDuration(converter.fromRow(row, prefix + "_free_days_duration", Duration.class));
        entity.setDiscountPercentage(converter.fromRow(row, prefix + "_discount_percentage", Float.class));
        entity.setStartDate(converter.fromRow(row, prefix + "_start_date", LocalDate.class));
        entity.setEndDate(converter.fromRow(row, prefix + "_end_date", LocalDate.class));
        entity.setSubscriptionsLimit(converter.fromRow(row, prefix + "_subscriptions_limit", Integer.class));
        entity.setLinkCode(converter.fromRow(row, prefix + "_link_code", String.class));
        entity.setIsFinished(converter.fromRow(row, prefix + "_is_finished", Boolean.class));
        entity.setPromotionType(converter.fromRow(row, prefix + "_promotion_type", OfferPromotionType.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatorId(converter.fromRow(row, prefix + "_creator_id", Long.class));
        return entity;
    }
}
