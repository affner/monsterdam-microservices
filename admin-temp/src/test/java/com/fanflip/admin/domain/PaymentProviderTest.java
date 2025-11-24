package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.PaymentProviderTestSamples.*;
import static com.monsterdam.admin.domain.PaymentTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PaymentProviderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentProvider.class);
        PaymentProvider paymentProvider1 = getPaymentProviderSample1();
        PaymentProvider paymentProvider2 = new PaymentProvider();
        assertThat(paymentProvider1).isNotEqualTo(paymentProvider2);

        paymentProvider2.setId(paymentProvider1.getId());
        assertThat(paymentProvider1).isEqualTo(paymentProvider2);

        paymentProvider2 = getPaymentProviderSample2();
        assertThat(paymentProvider1).isNotEqualTo(paymentProvider2);
    }

    @Test
    void paymentsTest() throws Exception {
        PaymentProvider paymentProvider = getPaymentProviderRandomSampleGenerator();
        PaymentTransaction paymentTransactionBack = getPaymentTransactionRandomSampleGenerator();

        paymentProvider.addPayments(paymentTransactionBack);
        assertThat(paymentProvider.getPayments()).containsOnly(paymentTransactionBack);
        assertThat(paymentTransactionBack.getPaymentProvider()).isEqualTo(paymentProvider);

        paymentProvider.removePayments(paymentTransactionBack);
        assertThat(paymentProvider.getPayments()).doesNotContain(paymentTransactionBack);
        assertThat(paymentTransactionBack.getPaymentProvider()).isNull();

        paymentProvider.payments(new HashSet<>(Set.of(paymentTransactionBack)));
        assertThat(paymentProvider.getPayments()).containsOnly(paymentTransactionBack);
        assertThat(paymentTransactionBack.getPaymentProvider()).isEqualTo(paymentProvider);

        paymentProvider.setPayments(new HashSet<>());
        assertThat(paymentProvider.getPayments()).doesNotContain(paymentTransactionBack);
        assertThat(paymentTransactionBack.getPaymentProvider()).isNull();
    }
}
