package com.fanflip.finance.domain;

import static com.fanflip.finance.domain.CreatorEarningTestSamples.*;
import static com.fanflip.finance.domain.PaymentTransactionTestSamples.*;
import static com.fanflip.finance.domain.PurchasedContentTestSamples.*;
import static com.fanflip.finance.domain.WalletTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.finance.web.rest.TestUtil;
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
}
