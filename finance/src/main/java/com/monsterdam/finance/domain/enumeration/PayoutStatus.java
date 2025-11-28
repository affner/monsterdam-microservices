package com.monsterdam.finance.domain.enumeration;

/**
 * The PayoutStatus enumeration.
 */
public enum PayoutStatus {
    WITHDRAW_PENDING("money.withdraw.status.pending"),
    WITHDRAW_PROCESSED("money.withdraw.status.processed"),
    WITHDRAW_DECLINED("money.withdraw.status.declined");

    private final String value;

    PayoutStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
