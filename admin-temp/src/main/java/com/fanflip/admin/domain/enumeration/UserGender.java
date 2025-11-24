package com.monsterdam.admin.domain.enumeration;

/**
 * The UserGender enumeration.
 */
public enum UserGender {
    MALE("user.gender.male"),
    FEMALE("user.gender.female"),
    TRANS_FEMALE("user.gender.trans-female"),
    TRANS_MALE("user.gender.trans-male");

    private final String value;

    UserGender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
