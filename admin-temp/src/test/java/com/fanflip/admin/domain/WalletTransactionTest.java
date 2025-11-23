package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.PaymentTransactionTestSamples.*;
import static com.fanflip.admin.domain.PurchasedContentTestSamples.*;
import static com.fanflip.admin.domain.PurchasedSubscriptionTestSamples.*;
import static com.fanflip.admin.domain.PurchasedTipTestSamples.*;
import static com.fanflip.admin.domain.UserProfileTestSamples.*;
import static com.fanflip.admin.domain.WalletTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WalletTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WalletTransaction.class);
        WalletTransaction walletTransaction1 = getWalletTransactionSample1();
        WalletTransaction walletTransaction2 = new WalletTransaction();
        assertThat(walletTransaction1).isNotEqualTo(walletTransaction2);

        walletTransaction2.setId(walletTransaction1.getId());
        assertThat(walletTransaction1).isEqualTo(walletTransaction2);

        walletTransaction2 = getWalletTransactionSample2();
        assertThat(walletTransaction1).isNotEqualTo(walletTransaction2);
    }

    @Test
    void paymentTest() throws Exception {
        WalletTransaction walletTransaction = getWalletTransactionRandomSampleGenerator();
        PaymentTransaction paymentTransactionBack = getPaymentTransactionRandomSampleGenerator();

        walletTransaction.setPayment(paymentTransactionBack);
        assertThat(walletTransaction.getPayment()).isEqualTo(paymentTransactionBack);

        walletTransaction.payment(null);
        assertThat(walletTransaction.getPayment()).isNull();
    }

    @Test
    void viewerTest() throws Exception {
        WalletTransaction walletTransaction = getWalletTransactionRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        walletTransaction.setViewer(userProfileBack);
        assertThat(walletTransaction.getViewer()).isEqualTo(userProfileBack);

        walletTransaction.viewer(null);
        assertThat(walletTransaction.getViewer()).isNull();
    }

    @Test
    void purchasedContentTest() throws Exception {
        WalletTransaction walletTransaction = getWalletTransactionRandomSampleGenerator();
        PurchasedContent purchasedContentBack = getPurchasedContentRandomSampleGenerator();

        walletTransaction.setPurchasedContent(purchasedContentBack);
        assertThat(walletTransaction.getPurchasedContent()).isEqualTo(purchasedContentBack);
        assertThat(purchasedContentBack.getWalletTransaction()).isEqualTo(walletTransaction);

        walletTransaction.purchasedContent(null);
        assertThat(walletTransaction.getPurchasedContent()).isNull();
        assertThat(purchasedContentBack.getWalletTransaction()).isNull();
    }

    @Test
    void purchasedSubscriptionTest() throws Exception {
        WalletTransaction walletTransaction = getWalletTransactionRandomSampleGenerator();
        PurchasedSubscription purchasedSubscriptionBack = getPurchasedSubscriptionRandomSampleGenerator();

        walletTransaction.setPurchasedSubscription(purchasedSubscriptionBack);
        assertThat(walletTransaction.getPurchasedSubscription()).isEqualTo(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getWalletTransaction()).isEqualTo(walletTransaction);

        walletTransaction.purchasedSubscription(null);
        assertThat(walletTransaction.getPurchasedSubscription()).isNull();
        assertThat(purchasedSubscriptionBack.getWalletTransaction()).isNull();
    }

    @Test
    void purchasedTipTest() throws Exception {
        WalletTransaction walletTransaction = getWalletTransactionRandomSampleGenerator();
        PurchasedTip purchasedTipBack = getPurchasedTipRandomSampleGenerator();

        walletTransaction.setPurchasedTip(purchasedTipBack);
        assertThat(walletTransaction.getPurchasedTip()).isEqualTo(purchasedTipBack);
        assertThat(purchasedTipBack.getWalletTransaction()).isEqualTo(walletTransaction);

        walletTransaction.purchasedTip(null);
        assertThat(walletTransaction.getPurchasedTip()).isNull();
        assertThat(purchasedTipBack.getWalletTransaction()).isNull();
    }
}
