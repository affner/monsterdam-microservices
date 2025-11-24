package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.PaymentMethodTestSamples.*;
import static com.monsterdam.admin.domain.PaymentTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PaymentMethodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethod.class);
        PaymentMethod paymentMethod1 = getPaymentMethodSample1();
        PaymentMethod paymentMethod2 = new PaymentMethod();
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);

        paymentMethod2.setId(paymentMethod1.getId());
        assertThat(paymentMethod1).isEqualTo(paymentMethod2);

        paymentMethod2 = getPaymentMethodSample2();
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);
    }

    @Test
    void paymentsTest() throws Exception {
        PaymentMethod paymentMethod = getPaymentMethodRandomSampleGenerator();
        PaymentTransaction paymentTransactionBack = getPaymentTransactionRandomSampleGenerator();

        paymentMethod.addPayments(paymentTransactionBack);
        assertThat(paymentMethod.getPayments()).containsOnly(paymentTransactionBack);
        assertThat(paymentTransactionBack.getPaymentMethod()).isEqualTo(paymentMethod);

        paymentMethod.removePayments(paymentTransactionBack);
        assertThat(paymentMethod.getPayments()).doesNotContain(paymentTransactionBack);
        assertThat(paymentTransactionBack.getPaymentMethod()).isNull();

        paymentMethod.payments(new HashSet<>(Set.of(paymentTransactionBack)));
        assertThat(paymentMethod.getPayments()).containsOnly(paymentTransactionBack);
        assertThat(paymentTransactionBack.getPaymentMethod()).isEqualTo(paymentMethod);

        paymentMethod.setPayments(new HashSet<>());
        assertThat(paymentMethod.getPayments()).doesNotContain(paymentTransactionBack);
        assertThat(paymentTransactionBack.getPaymentMethod()).isNull();
    }
}
