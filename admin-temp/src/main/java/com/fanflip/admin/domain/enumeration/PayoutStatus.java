package com.monsterdam.admin.domain.enumeration;

/**
 * The PayoutStatus enumeration.
 */
public enum PayoutStatus {
    PENDING("money.withdraw.status.pending"),
    PROCESSED("money.withdraw.status.processed"),
    DECLINED("money.withdraw.status.declined");

    private final String value;

    PayoutStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
