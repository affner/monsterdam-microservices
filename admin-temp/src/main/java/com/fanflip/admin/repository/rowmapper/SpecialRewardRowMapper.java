package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.SpecialReward;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SpecialReward}, with proper type conversions.
 */
@Service
public class SpecialRewardRowMapper implements BiFunction<Row, String, SpecialReward> {

    private final ColumnConverter converter;

    public SpecialRewardRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SpecialReward} stored in the database.
     */
    @Override
    public SpecialReward apply(Row row, String prefix) {
        SpecialReward entity = new SpecialReward();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setContentPackageId(converter.fromRow(row, prefix + "_content_package_id", Long.class));
        entity.setViewerId(converter.fromRow(row, prefix + "_viewer_id", Long.class));
        entity.setOfferPromotionId(converter.fromRow(row, prefix + "_offer_promotion_id", Long.class));
        return entity;
    }
}
