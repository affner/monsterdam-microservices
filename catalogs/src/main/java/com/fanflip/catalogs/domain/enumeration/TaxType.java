package com.monsterdam.catalogs.domain.enumeration;

/**
 * The TaxType enumeration.
 */
public enum TaxType {
    VAT("tax.type.vat"),
    WITHHOLDING("tax.type.withholding");

    private final String value;

    TaxType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
