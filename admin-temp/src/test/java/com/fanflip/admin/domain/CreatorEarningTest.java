package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.CreatorEarningTestSamples.*;
import static com.monsterdam.admin.domain.MoneyPayoutTestSamples.*;
import static com.monsterdam.admin.domain.PurchasedContentTestSamples.*;
import static com.monsterdam.admin.domain.PurchasedSubscriptionTestSamples.*;
import static com.monsterdam.admin.domain.PurchasedTipTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CreatorEarningTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreatorEarning.class);
        CreatorEarning creatorEarning1 = getCreatorEarningSample1();
        CreatorEarning creatorEarning2 = new CreatorEarning();
        assertThat(creatorEarning1).isNotEqualTo(creatorEarning2);

        creatorEarning2.setId(creatorEarning1.getId());
        assertThat(creatorEarning1).isEqualTo(creatorEarning2);

        creatorEarning2 = getCreatorEarningSample2();
        assertThat(creatorEarning1).isNotEqualTo(creatorEarning2);
    }

    @Test
    void creatorTest() throws Exception {
        CreatorEarning creatorEarning = getCreatorEarningRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        creatorEarning.setCreator(userProfileBack);
        assertThat(creatorEarning.getCreator()).isEqualTo(userProfileBack);

        creatorEarning.creator(null);
        assertThat(creatorEarning.getCreator()).isNull();
    }

    @Test
    void moneyPayoutTest() throws Exception {
        CreatorEarning creatorEarning = getCreatorEarningRandomSampleGenerator();
        MoneyPayout moneyPayoutBack = getMoneyPayoutRandomSampleGenerator();

        creatorEarning.setMoneyPayout(moneyPayoutBack);
        assertThat(creatorEarning.getMoneyPayout()).isEqualTo(moneyPayoutBack);
        assertThat(moneyPayoutBack.getCreatorEarning()).isEqualTo(creatorEarning);

        creatorEarning.moneyPayout(null);
        assertThat(creatorEarning.getMoneyPayout()).isNull();
        assertThat(moneyPayoutBack.getCreatorEarning()).isNull();
    }

    @Test
    void purchasedContentTest() throws Exception {
        CreatorEarning creatorEarning = getCreatorEarningRandomSampleGenerator();
        PurchasedContent purchasedContentBack = getPurchasedContentRandomSampleGenerator();

        creatorEarning.setPurchasedContent(purchasedContentBack);
        assertThat(creatorEarning.getPurchasedContent()).isEqualTo(purchasedContentBack);
        assertThat(purchasedContentBack.getCreatorEarning()).isEqualTo(creatorEarning);

        creatorEarning.purchasedContent(null);
        assertThat(creatorEarning.getPurchasedContent()).isNull();
        assertThat(purchasedContentBack.getCreatorEarning()).isNull();
    }

    @Test
    void purchasedSubscriptionTest() throws Exception {
        CreatorEarning creatorEarning = getCreatorEarningRandomSampleGenerator();
        PurchasedSubscription purchasedSubscriptionBack = getPurchasedSubscriptionRandomSampleGenerator();

        creatorEarning.setPurchasedSubscription(purchasedSubscriptionBack);
        assertThat(creatorEarning.getPurchasedSubscription()).isEqualTo(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getCreatorEarning()).isEqualTo(creatorEarning);

        creatorEarning.purchasedSubscription(null);
        assertThat(creatorEarning.getPurchasedSubscription()).isNull();
        assertThat(purchasedSubscriptionBack.getCreatorEarning()).isNull();
    }

    @Test
    void purchasedTipTest() throws Exception {
        CreatorEarning creatorEarning = getCreatorEarningRandomSampleGenerator();
        PurchasedTip purchasedTipBack = getPurchasedTipRandomSampleGenerator();

        creatorEarning.setPurchasedTip(purchasedTipBack);
        assertThat(creatorEarning.getPurchasedTip()).isEqualTo(purchasedTipBack);
        assertThat(purchasedTipBack.getCreatorEarning()).isEqualTo(creatorEarning);

        creatorEarning.purchasedTip(null);
        assertThat(creatorEarning.getPurchasedTip()).isNull();
        assertThat(purchasedTipBack.getCreatorEarning()).isNull();
    }
}
