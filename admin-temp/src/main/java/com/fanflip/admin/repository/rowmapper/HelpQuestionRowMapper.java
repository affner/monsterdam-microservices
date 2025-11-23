package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.HelpQuestion;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link HelpQuestion}, with proper type conversions.
 */
@Service
public class HelpQuestionRowMapper implements BiFunction<Row, String, HelpQuestion> {

    private final ColumnConverter converter;

    public HelpQuestionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HelpQuestion} stored in the database.
     */
    @Override
    public HelpQuestion apply(Row row, String prefix) {
        HelpQuestion entity = new HelpQuestion();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setSubcategoryId(converter.fromRow(row, prefix + "_subcategory_id", Long.class));
        return entity;
    }
}
