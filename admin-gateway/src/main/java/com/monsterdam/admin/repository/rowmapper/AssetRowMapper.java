package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.Asset;
import com.monsterdam.admin.domain.enumeration.AssetType;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Asset}, with proper type conversions.
 */
@Service
public class AssetRowMapper implements BiFunction<Row, String, Asset> {

    private final ColumnConverter converter;

    public AssetRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Asset} stored in the database.
     */
    @Override
    public Asset apply(Row row, String prefix) {
        Asset entity = new Asset();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setValue(converter.fromRow(row, prefix + "_value", BigDecimal.class));
        entity.setAcquisitionDate(converter.fromRow(row, prefix + "_acquisition_date", LocalDate.class));
        entity.setType(converter.fromRow(row, prefix + "_type", AssetType.class));
        return entity;
    }
}
