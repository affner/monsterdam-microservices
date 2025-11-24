package com.monsterdam.admin.domain.enumeration;

/**
 * The HashtagType enumeration.
 */
public enum HashtagType {
    USER("hashtag.type.user"),
    POST("hashtag.type.post"),
    FETISH("hashtag.type.fetish");

    private final String value;

    HashtagType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
