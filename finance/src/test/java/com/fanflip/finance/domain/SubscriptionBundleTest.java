package com.fanflip.finance.domain;

import static com.fanflip.finance.domain.PurchasedSubscriptionTestSamples.*;
import static com.fanflip.finance.domain.SubscriptionBundleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.finance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SubscriptionBundleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionBundle.class);
        SubscriptionBundle subscriptionBundle1 = getSubscriptionBundleSample1();
        SubscriptionBundle subscriptionBundle2 = new SubscriptionBundle();
        assertThat(subscriptionBundle1).isNotEqualTo(subscriptionBundle2);

        subscriptionBundle2.setId(subscriptionBundle1.getId());
        assertThat(subscriptionBundle1).isEqualTo(subscriptionBundle2);

        subscriptionBundle2 = getSubscriptionBundleSample2();
        assertThat(subscriptionBundle1).isNotEqualTo(subscriptionBundle2);
    }

    @Test
    void selledSubscriptionsTest() throws Exception {
        SubscriptionBundle subscriptionBundle = getSubscriptionBundleRandomSampleGenerator();
        PurchasedSubscription purchasedSubscriptionBack = getPurchasedSubscriptionRandomSampleGenerator();

        subscriptionBundle.addSelledSubscriptions(purchasedSubscriptionBack);
        assertThat(subscriptionBundle.getSelledSubscriptions()).containsOnly(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getSubscriptionBundle()).isEqualTo(subscriptionBundle);

        subscriptionBundle.removeSelledSubscriptions(purchasedSubscriptionBack);
        assertThat(subscriptionBundle.getSelledSubscriptions()).doesNotContain(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getSubscriptionBundle()).isNull();

        subscriptionBundle.selledSubscriptions(new HashSet<>(Set.of(purchasedSubscriptionBack)));
        assertThat(subscriptionBundle.getSelledSubscriptions()).containsOnly(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getSubscriptionBundle()).isEqualTo(subscriptionBundle);

        subscriptionBundle.setSelledSubscriptions(new HashSet<>());
        assertThat(subscriptionBundle.getSelledSubscriptions()).doesNotContain(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getSubscriptionBundle()).isNull();
    }
}
