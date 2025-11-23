package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.OfferPromotionTestSamples.*;
import static com.fanflip.admin.domain.PurchasedSubscriptionTestSamples.*;
import static com.fanflip.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OfferPromotionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OfferPromotion.class);
        OfferPromotion offerPromotion1 = getOfferPromotionSample1();
        OfferPromotion offerPromotion2 = new OfferPromotion();
        assertThat(offerPromotion1).isNotEqualTo(offerPromotion2);

        offerPromotion2.setId(offerPromotion1.getId());
        assertThat(offerPromotion1).isEqualTo(offerPromotion2);

        offerPromotion2 = getOfferPromotionSample2();
        assertThat(offerPromotion1).isNotEqualTo(offerPromotion2);
    }

    @Test
    void purchasedSubscriptionsTest() throws Exception {
        OfferPromotion offerPromotion = getOfferPromotionRandomSampleGenerator();
        PurchasedSubscription purchasedSubscriptionBack = getPurchasedSubscriptionRandomSampleGenerator();

        offerPromotion.addPurchasedSubscriptions(purchasedSubscriptionBack);
        assertThat(offerPromotion.getPurchasedSubscriptions()).containsOnly(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getAppliedPromotion()).isEqualTo(offerPromotion);

        offerPromotion.removePurchasedSubscriptions(purchasedSubscriptionBack);
        assertThat(offerPromotion.getPurchasedSubscriptions()).doesNotContain(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getAppliedPromotion()).isNull();

        offerPromotion.purchasedSubscriptions(new HashSet<>(Set.of(purchasedSubscriptionBack)));
        assertThat(offerPromotion.getPurchasedSubscriptions()).containsOnly(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getAppliedPromotion()).isEqualTo(offerPromotion);

        offerPromotion.setPurchasedSubscriptions(new HashSet<>());
        assertThat(offerPromotion.getPurchasedSubscriptions()).doesNotContain(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getAppliedPromotion()).isNull();
    }

    @Test
    void creatorTest() throws Exception {
        OfferPromotion offerPromotion = getOfferPromotionRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        offerPromotion.setCreator(userProfileBack);
        assertThat(offerPromotion.getCreator()).isEqualTo(userProfileBack);

        offerPromotion.creator(null);
        assertThat(offerPromotion.getCreator()).isNull();
    }
}
