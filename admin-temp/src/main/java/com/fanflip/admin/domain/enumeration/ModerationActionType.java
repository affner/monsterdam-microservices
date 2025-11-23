package com.fanflip.admin.domain.enumeration;

/**
 * The ModerationActionType enumeration.
 */
public enum ModerationActionType {
    WARNING("admin.moderation.action.type.warning"),
    TEMPORARY_BAN("admin.moderation.action.type.temporary_ban"),
    PERMANENT_BAN("admin.moderation.action.type.permanent_ban"),
    CONTENT_REMOVAL("admin.moderation.action.type.content_removal"),
    OTHER("admin.moderation.action.type.other");

    private final String value;

    ModerationActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
