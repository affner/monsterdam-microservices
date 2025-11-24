package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.HelpRelatedArticle;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link HelpRelatedArticle}, with proper type conversions.
 */
@Service
public class HelpRelatedArticleRowMapper implements BiFunction<Row, String, HelpRelatedArticle> {

    private final ColumnConverter converter;

    public HelpRelatedArticleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HelpRelatedArticle} stored in the database.
     */
    @Override
    public HelpRelatedArticle apply(Row row, String prefix) {
        HelpRelatedArticle entity = new HelpRelatedArticle();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        return entity;
    }
}
