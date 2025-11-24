package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.ContentPackageTestSamples.*;
import static com.monsterdam.admin.domain.CreatorEarningTestSamples.*;
import static com.monsterdam.admin.domain.PaymentTransactionTestSamples.*;
import static com.monsterdam.admin.domain.PurchasedContentTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static com.monsterdam.admin.domain.WalletTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchasedContentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasedContent.class);
        PurchasedContent purchasedContent1 = getPurchasedContentSample1();
        PurchasedContent purchasedContent2 = new PurchasedContent();
        assertThat(purchasedContent1).isNotEqualTo(purchasedContent2);

        purchasedContent2.setId(purchasedContent1.getId());
        assertThat(purchasedContent1).isEqualTo(purchasedContent2);

        purchasedContent2 = getPurchasedContentSample2();
        assertThat(purchasedContent1).isNotEqualTo(purchasedContent2);
    }

    @Test
    void paymentTest() throws Exception {
        PurchasedContent purchasedContent = getPurchasedContentRandomSampleGenerator();
        PaymentTransaction paymentTransactionBack = getPaymentTransactionRandomSampleGenerator();

        purchasedContent.setPayment(paymentTransactionBack);
        assertThat(purchasedContent.getPayment()).isEqualTo(paymentTransactionBack);

        purchasedContent.payment(null);
        assertThat(purchasedContent.getPayment()).isNull();
    }

    @Test
    void walletTransactionTest() throws Exception {
        PurchasedContent purchasedContent = getPurchasedContentRandomSampleGenerator();
        WalletTransaction walletTransactionBack = getWalletTransactionRandomSampleGenerator();

        purchasedContent.setWalletTransaction(walletTransactionBack);
        assertThat(purchasedContent.getWalletTransaction()).isEqualTo(walletTransactionBack);

        purchasedContent.walletTransaction(null);
        assertThat(purchasedContent.getWalletTransaction()).isNull();
    }

    @Test
    void creatorEarningTest() throws Exception {
        PurchasedContent purchasedContent = getPurchasedContentRandomSampleGenerator();
        CreatorEarning creatorEarningBack = getCreatorEarningRandomSampleGenerator();

        purchasedContent.setCreatorEarning(creatorEarningBack);
        assertThat(purchasedContent.getCreatorEarning()).isEqualTo(creatorEarningBack);

        purchasedContent.creatorEarning(null);
        assertThat(purchasedContent.getCreatorEarning()).isNull();
    }

    @Test
    void viewerTest() throws Exception {
        PurchasedContent purchasedContent = getPurchasedContentRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        purchasedContent.setViewer(userProfileBack);
        assertThat(purchasedContent.getViewer()).isEqualTo(userProfileBack);

        purchasedContent.viewer(null);
        assertThat(purchasedContent.getViewer()).isNull();
    }

    @Test
    void purchasedContentPackageTest() throws Exception {
        PurchasedContent purchasedContent = getPurchasedContentRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        purchasedContent.setPurchasedContentPackage(contentPackageBack);
        assertThat(purchasedContent.getPurchasedContentPackage()).isEqualTo(contentPackageBack);

        purchasedContent.purchasedContentPackage(null);
        assertThat(purchasedContent.getPurchasedContentPackage()).isNull();
    }
}
