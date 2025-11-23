package com.fanflip.finance.domain.enumeration;

/**
 * The PurchasedSubscriptionStatus enumeration.
 */
public enum PurchasedSubscriptionStatus {
    PURCHASED("subscription.status.purchased"),
    PENDING("subscription.status.pending"),
    EXPIRED("subscription.status.expired"),
    CANCELLED("subscription.status.cancelled"),
    SUSPENDED("subscription.status.suspended");

    private final String value;

    PurchasedSubscriptionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
