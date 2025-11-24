package com.monsterdam.finance.domain.enumeration;

/**
 * The GenericStatus enumeration.
 */
public enum GenericStatus {
    PENDING("generic.status.pending"),
    COMPLETED("generic.status.completed"),
    DECLINED("generic.status.declined"),
    REFUNDED("generic.status.refunded"),
    CANCELED("generic.status.canceled");

    private final String value;

    GenericStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
