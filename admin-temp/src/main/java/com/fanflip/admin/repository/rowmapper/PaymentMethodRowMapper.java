package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.PaymentMethod;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PaymentMethod}, with proper type conversions.
 */
@Service
public class PaymentMethodRowMapper implements BiFunction<Row, String, PaymentMethod> {

    private final ColumnConverter converter;

    public PaymentMethodRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PaymentMethod} stored in the database.
     */
    @Override
    public PaymentMethod apply(Row row, String prefix) {
        PaymentMethod entity = new PaymentMethod();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setMethodName(converter.fromRow(row, prefix + "_method_name", String.class));
        entity.setTokenText(converter.fromRow(row, prefix + "_token_text", String.class));
        entity.setExpirationDate(converter.fromRow(row, prefix + "_expiration_date", LocalDate.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
