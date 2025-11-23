package com.fanflip.admin.domain.enumeration;

/**
 * The UserLanguage enumeration.
 */
public enum UserLanguage {
    ES("user.language.es"),
    EN("user.language.en"),
    FR("user.language.fr"),
    DE("user.language.de"),
    PT_BR("user.language.pt_br"),
    RU("user.language.ru");

    private final String value;

    UserLanguage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
