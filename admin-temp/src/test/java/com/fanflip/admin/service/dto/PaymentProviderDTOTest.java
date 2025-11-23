package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentProviderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentProviderDTO.class);
        PaymentProviderDTO paymentProviderDTO1 = new PaymentProviderDTO();
        paymentProviderDTO1.setId(1L);
        PaymentProviderDTO paymentProviderDTO2 = new PaymentProviderDTO();
        assertThat(paymentProviderDTO1).isNotEqualTo(paymentProviderDTO2);
        paymentProviderDTO2.setId(paymentProviderDTO1.getId());
        assertThat(paymentProviderDTO1).isEqualTo(paymentProviderDTO2);
        paymentProviderDTO2.setId(2L);
        assertThat(paymentProviderDTO1).isNotEqualTo(paymentProviderDTO2);
        paymentProviderDTO1.setId(null);
        assertThat(paymentProviderDTO1).isNotEqualTo(paymentProviderDTO2);
    }
}
