package com.fanflip.admin.domain.enumeration;

/**
 * The ReportStatus enumeration.
 */
public enum ReportStatus {
    PENDING("report.status.pending"),
    REVIEWED("report.status.reviewed"),
    ACTION_TAKEN("report.status.action-taken"),
    DISMISSED("report.status.dismissed");

    private final String value;

    ReportStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
