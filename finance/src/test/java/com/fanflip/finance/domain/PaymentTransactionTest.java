package com.fanflip.finance.domain;

import static com.fanflip.finance.domain.PaymentTransactionTestSamples.*;
import static com.fanflip.finance.domain.PurchasedContentTestSamples.*;
import static com.fanflip.finance.domain.PurchasedSubscriptionTestSamples.*;
import static com.fanflip.finance.domain.PurchasedTipTestSamples.*;
import static com.fanflip.finance.domain.WalletTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.finance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentTransaction.class);
        PaymentTransaction paymentTransaction1 = getPaymentTransactionSample1();
        PaymentTransaction paymentTransaction2 = new PaymentTransaction();
        assertThat(paymentTransaction1).isNotEqualTo(paymentTransaction2);

        paymentTransaction2.setId(paymentTransaction1.getId());
        assertThat(paymentTransaction1).isEqualTo(paymentTransaction2);

        paymentTransaction2 = getPaymentTransactionSample2();
        assertThat(paymentTransaction1).isNotEqualTo(paymentTransaction2);
    }

    @Test
    void walletTransactionTest() throws Exception {
        PaymentTransaction paymentTransaction = getPaymentTransactionRandomSampleGenerator();
        WalletTransaction walletTransactionBack = getWalletTransactionRandomSampleGenerator();

        paymentTransaction.setWalletTransaction(walletTransactionBack);
        assertThat(paymentTransaction.getWalletTransaction()).isEqualTo(walletTransactionBack);
        assertThat(walletTransactionBack.getPayment()).isEqualTo(paymentTransaction);

        paymentTransaction.walletTransaction(null);
        assertThat(paymentTransaction.getWalletTransaction()).isNull();
        assertThat(walletTransactionBack.getPayment()).isNull();
    }

    @Test
    void purchasedTipTest() throws Exception {
        PaymentTransaction paymentTransaction = getPaymentTransactionRandomSampleGenerator();
        PurchasedTip purchasedTipBack = getPurchasedTipRandomSampleGenerator();

        paymentTransaction.setPurchasedTip(purchasedTipBack);
        assertThat(paymentTransaction.getPurchasedTip()).isEqualTo(purchasedTipBack);
        assertThat(purchasedTipBack.getPayment()).isEqualTo(paymentTransaction);

        paymentTransaction.purchasedTip(null);
        assertThat(paymentTransaction.getPurchasedTip()).isNull();
        assertThat(purchasedTipBack.getPayment()).isNull();
    }

    @Test
    void purchasedContentTest() throws Exception {
        PaymentTransaction paymentTransaction = getPaymentTransactionRandomSampleGenerator();
        PurchasedContent purchasedContentBack = getPurchasedContentRandomSampleGenerator();

        paymentTransaction.setPurchasedContent(purchasedContentBack);
        assertThat(paymentTransaction.getPurchasedContent()).isEqualTo(purchasedContentBack);
        assertThat(purchasedContentBack.getPayment()).isEqualTo(paymentTransaction);

        paymentTransaction.purchasedContent(null);
        assertThat(paymentTransaction.getPurchasedContent()).isNull();
        assertThat(purchasedContentBack.getPayment()).isNull();
    }

    @Test
    void purchasedSubscriptionTest() throws Exception {
        PaymentTransaction paymentTransaction = getPaymentTransactionRandomSampleGenerator();
        PurchasedSubscription purchasedSubscriptionBack = getPurchasedSubscriptionRandomSampleGenerator();

        paymentTransaction.setPurchasedSubscription(purchasedSubscriptionBack);
        assertThat(paymentTransaction.getPurchasedSubscription()).isEqualTo(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getPayment()).isEqualTo(paymentTransaction);

        paymentTransaction.purchasedSubscription(null);
        assertThat(paymentTransaction.getPurchasedSubscription()).isNull();
        assertThat(purchasedSubscriptionBack.getPayment()).isNull();
    }
}
