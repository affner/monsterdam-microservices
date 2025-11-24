package com.monsterdam.admin.domain.enumeration;

/**
 * The UserEventStatus enumeration.
 */
public enum UserEventStatus {
    CANCELED("creator.event.status.canceled"),
    ACTIVE("creator.event.status.active"),
    DELETED("creator.event.status.deleted");

    private final String value;

    UserEventStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
