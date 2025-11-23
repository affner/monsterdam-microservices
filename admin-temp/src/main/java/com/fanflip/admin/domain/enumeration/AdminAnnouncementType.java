package com.fanflip.admin.domain.enumeration;

/**
 * The AdminAnnouncementType enumeration.
 */
public enum AdminAnnouncementType {
    BANNER("admin.announcement"),
    SYSTEM_UPDATE("admin.announcement.type.system_update"),
    POLICY_CHANGE("admin.announcement.type.policy_change"),
    COMMUNITY_ALERT("admin.announcement.type.community_alert"),
    OTHER("admin.announcement.type.other");

    private final String value;

    AdminAnnouncementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
