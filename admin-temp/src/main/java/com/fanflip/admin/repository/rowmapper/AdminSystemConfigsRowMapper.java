package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.AdminSystemConfigs;
import com.fanflip.admin.domain.enumeration.ConfigurationCategory;
import com.fanflip.admin.domain.enumeration.ConfigurationValueType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AdminSystemConfigs}, with proper type conversions.
 */
@Service
public class AdminSystemConfigsRowMapper implements BiFunction<Row, String, AdminSystemConfigs> {

    private final ColumnConverter converter;

    public AdminSystemConfigsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AdminSystemConfigs} stored in the database.
     */
    @Override
    public AdminSystemConfigs apply(Row row, String prefix) {
        AdminSystemConfigs entity = new AdminSystemConfigs();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setConfigKey(converter.fromRow(row, prefix + "_config_key", String.class));
        entity.setConfigValue(converter.fromRow(row, prefix + "_config_value", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setConfigValueType(converter.fromRow(row, prefix + "_config_value_type", ConfigurationValueType.class));
        entity.setConfigCategory(converter.fromRow(row, prefix + "_config_category", ConfigurationCategory.class));
        entity.setConfigFileContentType(converter.fromRow(row, prefix + "_config_file_content_type", String.class));
        entity.setConfigFile(converter.fromRow(row, prefix + "_config_file", byte[].class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        return entity;
    }
}
