package com.fanflip.admin.domain.enumeration;

/**
 * The FeedbackType enumeration.
 */
public enum FeedbackType {
    ERROR("feedback.type.error"),
    SUGGESTION("feedback.type.suggestion"),
    PRAISE("feedback.type.praise"),
    OTHER("feedback.type.other");

    private final String value;

    FeedbackType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
