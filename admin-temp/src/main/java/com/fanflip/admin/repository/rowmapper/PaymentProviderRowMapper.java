package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.PaymentProvider;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PaymentProvider}, with proper type conversions.
 */
@Service
public class PaymentProviderRowMapper implements BiFunction<Row, String, PaymentProvider> {

    private final ColumnConverter converter;

    public PaymentProviderRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PaymentProvider} stored in the database.
     */
    @Override
    public PaymentProvider apply(Row row, String prefix) {
        PaymentProvider entity = new PaymentProvider();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setProviderName(converter.fromRow(row, prefix + "_provider_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setApiKeyText(converter.fromRow(row, prefix + "_api_key_text", String.class));
        entity.setApiSecretText(converter.fromRow(row, prefix + "_api_secret_text", String.class));
        entity.setEndpointText(converter.fromRow(row, prefix + "_endpoint_text", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
