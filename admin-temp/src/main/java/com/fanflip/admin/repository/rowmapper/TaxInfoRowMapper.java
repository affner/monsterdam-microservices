package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.TaxInfo;
import com.fanflip.admin.domain.enumeration.TaxType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TaxInfo}, with proper type conversions.
 */
@Service
public class TaxInfoRowMapper implements BiFunction<Row, String, TaxInfo> {

    private final ColumnConverter converter;

    public TaxInfoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TaxInfo} stored in the database.
     */
    @Override
    public TaxInfo apply(Row row, String prefix) {
        TaxInfo entity = new TaxInfo();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setRatePercentage(converter.fromRow(row, prefix + "_rate_percentage", Float.class));
        entity.setTaxType(converter.fromRow(row, prefix + "_tax_type", TaxType.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCountryId(converter.fromRow(row, prefix + "_country_id", Long.class));
        return entity;
    }
}
