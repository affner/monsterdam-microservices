package com.fanflip.admin.domain.enumeration;

/**
 * The ConfigurationValueType enumeration.
 */
public enum ConfigurationValueType {
    STRING("configuration.value.type.string"),
    INTEGER("configuration.value.type.integer"),
    BOOLEAN("configuration.value.type.boolean"),
    FLOAT("configuration.value.type.float"),
    DATE("configuration.value.type.date"),
    JSON("configuration.value.type.json"),
    TEXT("configuration.value.type.text");

    private final String value;

    ConfigurationValueType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
