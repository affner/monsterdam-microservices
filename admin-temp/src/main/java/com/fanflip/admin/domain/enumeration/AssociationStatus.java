package com.fanflip.admin.domain.enumeration;

/**
 * The AssociationStatus enumeration.
 */
public enum AssociationStatus {
    REQUESTED("association.status.requested"),
    APPROVED("association.status.approved"),
    REJECTED("association.status.rejected");

    private final String value;

    AssociationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
