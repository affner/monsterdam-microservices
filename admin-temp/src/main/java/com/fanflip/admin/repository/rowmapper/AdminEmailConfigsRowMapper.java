package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.AdminEmailConfigs;
import com.monsterdam.admin.domain.enumeration.EmailTemplateType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AdminEmailConfigs}, with proper type conversions.
 */
@Service
public class AdminEmailConfigsRowMapper implements BiFunction<Row, String, AdminEmailConfigs> {

    private final ColumnConverter converter;

    public AdminEmailConfigsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AdminEmailConfigs} stored in the database.
     */
    @Override
    public AdminEmailConfigs apply(Row row, String prefix) {
        AdminEmailConfigs entity = new AdminEmailConfigs();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setSubject(converter.fromRow(row, prefix + "_subject", String.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setMailTemplateType(converter.fromRow(row, prefix + "_mail_template_type", EmailTemplateType.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        return entity;
    }
}
