package com.monsterdam.admin.domain.enumeration;

/**
 * The DocumentStatus enumeration.
 */
public enum DocumentStatus {
    PENDING("admin.document.status.pending"),
    APPROVED("admin.document.status.approved"),
    REJECTED("admin.document.status.rejected");

    private final String value;

    DocumentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
