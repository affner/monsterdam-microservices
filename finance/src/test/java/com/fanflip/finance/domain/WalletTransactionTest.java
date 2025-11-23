package com.fanflip.finance.domain;

import static com.fanflip.finance.domain.PaymentTransactionTestSamples.*;
import static com.fanflip.finance.domain.PurchasedContentTestSamples.*;
import static com.fanflip.finance.domain.PurchasedSubscriptionTestSamples.*;
import static com.fanflip.finance.domain.PurchasedTipTestSamples.*;
import static com.fanflip.finance.domain.WalletTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.finance.web.rest.TestUtil;
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
}
