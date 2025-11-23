package com.fanflip.finance.domain;

import static com.fanflip.finance.domain.CreatorEarningTestSamples.*;
import static com.fanflip.finance.domain.OfferPromotionTestSamples.*;
import static com.fanflip.finance.domain.PaymentTransactionTestSamples.*;
import static com.fanflip.finance.domain.PurchasedSubscriptionTestSamples.*;
import static com.fanflip.finance.domain.SubscriptionBundleTestSamples.*;
import static com.fanflip.finance.domain.WalletTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.finance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchasedSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasedSubscription.class);
        PurchasedSubscription purchasedSubscription1 = getPurchasedSubscriptionSample1();
        PurchasedSubscription purchasedSubscription2 = new PurchasedSubscription();
        assertThat(purchasedSubscription1).isNotEqualTo(purchasedSubscription2);

        purchasedSubscription2.setId(purchasedSubscription1.getId());
        assertThat(purchasedSubscription1).isEqualTo(purchasedSubscription2);

        purchasedSubscription2 = getPurchasedSubscriptionSample2();
        assertThat(purchasedSubscription1).isNotEqualTo(purchasedSubscription2);
    }

    @Test
    void paymentTest() throws Exception {
        PurchasedSubscription purchasedSubscription = getPurchasedSubscriptionRandomSampleGenerator();
        PaymentTransaction paymentTransactionBack = getPaymentTransactionRandomSampleGenerator();

        purchasedSubscription.setPayment(paymentTransactionBack);
        assertThat(purchasedSubscription.getPayment()).isEqualTo(paymentTransactionBack);

        purchasedSubscription.payment(null);
        assertThat(purchasedSubscription.getPayment()).isNull();
    }

    @Test
    void walletTransactionTest() throws Exception {
        PurchasedSubscription purchasedSubscription = getPurchasedSubscriptionRandomSampleGenerator();
        WalletTransaction walletTransactionBack = getWalletTransactionRandomSampleGenerator();

        purchasedSubscription.setWalletTransaction(walletTransactionBack);
        assertThat(purchasedSubscription.getWalletTransaction()).isEqualTo(walletTransactionBack);

        purchasedSubscription.walletTransaction(null);
        assertThat(purchasedSubscription.getWalletTransaction()).isNull();
    }

    @Test
    void creatorEarningTest() throws Exception {
        PurchasedSubscription purchasedSubscription = getPurchasedSubscriptionRandomSampleGenerator();
        CreatorEarning creatorEarningBack = getCreatorEarningRandomSampleGenerator();

        purchasedSubscription.setCreatorEarning(creatorEarningBack);
        assertThat(purchasedSubscription.getCreatorEarning()).isEqualTo(creatorEarningBack);

        purchasedSubscription.creatorEarning(null);
        assertThat(purchasedSubscription.getCreatorEarning()).isNull();
    }

    @Test
    void subscriptionBundleTest() throws Exception {
        PurchasedSubscription purchasedSubscription = getPurchasedSubscriptionRandomSampleGenerator();
        SubscriptionBundle subscriptionBundleBack = getSubscriptionBundleRandomSampleGenerator();

        purchasedSubscription.setSubscriptionBundle(subscriptionBundleBack);
        assertThat(purchasedSubscription.getSubscriptionBundle()).isEqualTo(subscriptionBundleBack);

        purchasedSubscription.subscriptionBundle(null);
        assertThat(purchasedSubscription.getSubscriptionBundle()).isNull();
    }

    @Test
    void appliedPromotionTest() throws Exception {
        PurchasedSubscription purchasedSubscription = getPurchasedSubscriptionRandomSampleGenerator();
        OfferPromotion offerPromotionBack = getOfferPromotionRandomSampleGenerator();

        purchasedSubscription.setAppliedPromotion(offerPromotionBack);
        assertThat(purchasedSubscription.getAppliedPromotion()).isEqualTo(offerPromotionBack);

        purchasedSubscription.appliedPromotion(null);
        assertThat(purchasedSubscription.getAppliedPromotion()).isNull();
    }
}
