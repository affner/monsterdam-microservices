package com.fanflip.finance.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.finance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriptionBundleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionBundleDTO.class);
        SubscriptionBundleDTO subscriptionBundleDTO1 = new SubscriptionBundleDTO();
        subscriptionBundleDTO1.setId(1L);
        SubscriptionBundleDTO subscriptionBundleDTO2 = new SubscriptionBundleDTO();
        assertThat(subscriptionBundleDTO1).isNotEqualTo(subscriptionBundleDTO2);
        subscriptionBundleDTO2.setId(subscriptionBundleDTO1.getId());
        assertThat(subscriptionBundleDTO1).isEqualTo(subscriptionBundleDTO2);
        subscriptionBundleDTO2.setId(2L);
        assertThat(subscriptionBundleDTO1).isNotEqualTo(subscriptionBundleDTO2);
        subscriptionBundleDTO1.setId(null);
        assertThat(subscriptionBundleDTO1).isNotEqualTo(subscriptionBundleDTO2);
    }
}
