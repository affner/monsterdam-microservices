package com.fanflip.profile.domain;

import static com.fanflip.profile.domain.SpecialAwardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.profile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialAwardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialAward.class);
        SpecialAward specialAward1 = getSpecialAwardSample1();
        SpecialAward specialAward2 = new SpecialAward();
        assertThat(specialAward1).isNotEqualTo(specialAward2);

        specialAward2.setId(specialAward1.getId());
        assertThat(specialAward1).isEqualTo(specialAward2);

        specialAward2 = getSpecialAwardSample2();
        assertThat(specialAward1).isNotEqualTo(specialAward2);
    }
}
