package com.monsterdam.admin.domain.enumeration;

/**
 * The OfferPromotionType enumeration.
 */
public enum OfferPromotionType {
    SPECIAL("offer.promotion.type.special"),
    TRIAL_LINK("offer.promotion.type.trial-link"),
    DISCOUNT("offer.promotion.type.discount"),
    FREE_DAYS("offer.promotion.type.free-days");

    private final String value;

    OfferPromotionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
