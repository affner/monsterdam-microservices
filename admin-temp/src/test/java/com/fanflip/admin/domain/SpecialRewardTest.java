package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.SpecialRewardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialRewardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialReward.class);
        SpecialReward specialReward1 = getSpecialRewardSample1();
        SpecialReward specialReward2 = new SpecialReward();
        assertThat(specialReward1).isNotEqualTo(specialReward2);

        specialReward2.setId(specialReward1.getId());
        assertThat(specialReward1).isEqualTo(specialReward2);

        specialReward2 = getSpecialRewardSample2();
        assertThat(specialReward1).isNotEqualTo(specialReward2);
    }
}
