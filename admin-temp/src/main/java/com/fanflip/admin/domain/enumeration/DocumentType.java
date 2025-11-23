package com.fanflip.admin.domain.enumeration;

/**
 * The DocumentType enumeration.
 */
public enum DocumentType {
    ID_VERIFICATION("admin.document.type.id_verification"),
    CONTRACT("admin.document.type.contract"),
    MISC("admin.document.type.misc");

    private final String value;

    DocumentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
