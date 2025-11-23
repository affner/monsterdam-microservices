package com.fanflip.admin.domain.enumeration;

/**
 * The AdminGender enumeration.
 */
public enum AdminGender {
    MALE("user.gender.male"),
    FEMALE("user.gender.female"),
    TRANS_FEMALE("user.gender.trans-female"),
    TRANS_MALE("user.gender.trans-male");

    private final String value;

    AdminGender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
