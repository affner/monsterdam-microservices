package com.monsterdam.admin.domain.enumeration;

/**
 * The ReportCategory enumeration.
 */
public enum ReportCategory {
    POST_REPORT("admin.report.post"),
    COMMENT_REPORT("admin.report.comment"),
    MESSAGE_REPORT("admin.report.message"),
    MULTIMEDIA_REPORT("admin.report.multimedia"),
    USER_REPORT("admin.report.user");

    private final String value;

    ReportCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
