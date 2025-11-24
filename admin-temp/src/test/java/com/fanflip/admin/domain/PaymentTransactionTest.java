package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.AccountingRecordTestSamples.*;
import static com.monsterdam.admin.domain.PaymentMethodTestSamples.*;
import static com.monsterdam.admin.domain.PaymentProviderTestSamples.*;
import static com.monsterdam.admin.domain.PaymentTransactionTestSamples.*;
import static com.monsterdam.admin.domain.PurchasedContentTestSamples.*;
import static com.monsterdam.admin.domain.PurchasedSubscriptionTestSamples.*;
import static com.monsterdam.admin.domain.PurchasedTipTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static com.monsterdam.admin.domain.WalletTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
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
    void paymentMethodTest() throws Exception {
        PaymentTransaction paymentTransaction = getPaymentTransactionRandomSampleGenerator();
        PaymentMethod paymentMethodBack = getPaymentMethodRandomSampleGenerator();

        paymentTransaction.setPaymentMethod(paymentMethodBack);
        assertThat(paymentTransaction.getPaymentMethod()).isEqualTo(paymentMethodBack);

        paymentTransaction.paymentMethod(null);
        assertThat(paymentTransaction.getPaymentMethod()).isNull();
    }

    @Test
    void paymentProviderTest() throws Exception {
        PaymentTransaction paymentTransaction = getPaymentTransactionRandomSampleGenerator();
        PaymentProvider paymentProviderBack = getPaymentProviderRandomSampleGenerator();

        paymentTransaction.setPaymentProvider(paymentProviderBack);
        assertThat(paymentTransaction.getPaymentProvider()).isEqualTo(paymentProviderBack);

        paymentTransaction.paymentProvider(null);
        assertThat(paymentTransaction.getPaymentProvider()).isNull();
    }

    @Test
    void viewerTest() throws Exception {
        PaymentTransaction paymentTransaction = getPaymentTransactionRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        paymentTransaction.setViewer(userProfileBack);
        assertThat(paymentTransaction.getViewer()).isEqualTo(userProfileBack);

        paymentTransaction.viewer(null);
        assertThat(paymentTransaction.getViewer()).isNull();
    }

    @Test
    void accountingRecordTest() throws Exception {
        PaymentTransaction paymentTransaction = getPaymentTransactionRandomSampleGenerator();
        AccountingRecord accountingRecordBack = getAccountingRecordRandomSampleGenerator();

        paymentTransaction.setAccountingRecord(accountingRecordBack);
        assertThat(paymentTransaction.getAccountingRecord()).isEqualTo(accountingRecordBack);
        assertThat(accountingRecordBack.getPayment()).isEqualTo(paymentTransaction);

        paymentTransaction.accountingRecord(null);
        assertThat(paymentTransaction.getAccountingRecord()).isNull();
        assertThat(accountingRecordBack.getPayment()).isNull();
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
}
