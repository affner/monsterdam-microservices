package com.fanflip.admin.domain.enumeration;

/**
 * The ConfigurationCategory enumeration.
 */
public enum ConfigurationCategory {
    GENERAL("configuration.category.general"),
    SECURITY("configuration.category.security"),
    NOTIFICATIONS("configuration.category.notifications"),
    USER_INTERFACE("configuration.category.user_interface"),
    PAYMENT("configuration.category.payment"),
    INTEGRATIONS("configuration.category.integrations"),
    CONTENT("configuration.category.content"),
    SEO("configuration.category.seo");

    private final String value;

    ConfigurationCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
