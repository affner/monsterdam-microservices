package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.HelpCategory;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link HelpCategory}, with proper type conversions.
 */
@Service
public class HelpCategoryRowMapper implements BiFunction<Row, String, HelpCategory> {

    private final ColumnConverter converter;

    public HelpCategoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HelpCategory} stored in the database.
     */
    @Override
    public HelpCategory apply(Row row, String prefix) {
        HelpCategory entity = new HelpCategory();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
