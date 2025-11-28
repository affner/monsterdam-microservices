package com.monsterdam.profile.domain.enumeration;

/**
 * The ContentPreference enumeration.
 */
public enum ContentPreference {
    ALL("content.preference.all"),
    STRAIGHT("content.preference.straight"),
    GAY("content.preference.gay"),
    TRANS("content.preference.trans");

    private final String value;

    ContentPreference(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
