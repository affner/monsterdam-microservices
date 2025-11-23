package com.fanflip.admin.domain.enumeration;

/**
 * The ReviewStatus enumeration.
 */
public enum ReviewStatus {
    REVIEWING("admin.review.status.reviewing"),
    APPROVED("admin.review.status.approved"),
    REJECTED("admin.review.status.rejected");

    private final String value;

    ReviewStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
