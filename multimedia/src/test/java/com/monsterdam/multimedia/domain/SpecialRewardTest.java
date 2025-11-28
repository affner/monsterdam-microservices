package com.monsterdam.multimedia.domain;

import static com.monsterdam.multimedia.domain.ContentPackageTestSamples.*;
import static com.monsterdam.multimedia.domain.SpecialRewardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.multimedia.web.rest.TestUtil;
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

    @Test
    void contentPackageTest() throws Exception {
        SpecialReward specialReward = getSpecialRewardRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        specialReward.setContentPackage(contentPackageBack);
        assertThat(specialReward.getContentPackage()).isEqualTo(contentPackageBack);

        specialReward.contentPackage(null);
        assertThat(specialReward.getContentPackage()).isNull();
    }
}
