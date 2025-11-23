package com.fanflip.finance.domain;

import static com.fanflip.finance.domain.CreatorEarningTestSamples.*;
import static com.fanflip.finance.domain.PaymentTransactionTestSamples.*;
import static com.fanflip.finance.domain.PurchasedTipTestSamples.*;
import static com.fanflip.finance.domain.WalletTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.finance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchasedTipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasedTip.class);
        PurchasedTip purchasedTip1 = getPurchasedTipSample1();
        PurchasedTip purchasedTip2 = new PurchasedTip();
        assertThat(purchasedTip1).isNotEqualTo(purchasedTip2);

        purchasedTip2.setId(purchasedTip1.getId());
        assertThat(purchasedTip1).isEqualTo(purchasedTip2);

        purchasedTip2 = getPurchasedTipSample2();
        assertThat(purchasedTip1).isNotEqualTo(purchasedTip2);
    }

    @Test
    void paymentTest() throws Exception {
        PurchasedTip purchasedTip = getPurchasedTipRandomSampleGenerator();
        PaymentTransaction paymentTransactionBack = getPaymentTransactionRandomSampleGenerator();

        purchasedTip.setPayment(paymentTransactionBack);
        assertThat(purchasedTip.getPayment()).isEqualTo(paymentTransactionBack);

        purchasedTip.payment(null);
        assertThat(purchasedTip.getPayment()).isNull();
    }

    @Test
    void walletTransactionTest() throws Exception {
        PurchasedTip purchasedTip = getPurchasedTipRandomSampleGenerator();
        WalletTransaction walletTransactionBack = getWalletTransactionRandomSampleGenerator();

        purchasedTip.setWalletTransaction(walletTransactionBack);
        assertThat(purchasedTip.getWalletTransaction()).isEqualTo(walletTransactionBack);

        purchasedTip.walletTransaction(null);
        assertThat(purchasedTip.getWalletTransaction()).isNull();
    }

    @Test
    void creatorEarningTest() throws Exception {
        PurchasedTip purchasedTip = getPurchasedTipRandomSampleGenerator();
        CreatorEarning creatorEarningBack = getCreatorEarningRandomSampleGenerator();

        purchasedTip.setCreatorEarning(creatorEarningBack);
        assertThat(purchasedTip.getCreatorEarning()).isEqualTo(creatorEarningBack);

        purchasedTip.creatorEarning(null);
        assertThat(purchasedTip.getCreatorEarning()).isNull();
    }
}
