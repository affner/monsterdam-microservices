package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.HelpSubcategory;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link HelpSubcategory}, with proper type conversions.
 */
@Service
public class HelpSubcategoryRowMapper implements BiFunction<Row, String, HelpSubcategory> {

    private final ColumnConverter converter;

    public HelpSubcategoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HelpSubcategory} stored in the database.
     */
    @Override
    public HelpSubcategory apply(Row row, String prefix) {
        HelpSubcategory entity = new HelpSubcategory();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCategoryId(converter.fromRow(row, prefix + "_category_id", Long.class));
        return entity;
    }
}
