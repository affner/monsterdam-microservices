package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.PayoutMethodTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PayoutMethodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayoutMethod.class);
        PayoutMethod payoutMethod1 = getPayoutMethodSample1();
        PayoutMethod payoutMethod2 = new PayoutMethod();
        assertThat(payoutMethod1).isNotEqualTo(payoutMethod2);

        payoutMethod2.setId(payoutMethod1.getId());
        assertThat(payoutMethod1).isEqualTo(payoutMethod2);

        payoutMethod2 = getPayoutMethodSample2();
        assertThat(payoutMethod1).isNotEqualTo(payoutMethod2);
    }
}
