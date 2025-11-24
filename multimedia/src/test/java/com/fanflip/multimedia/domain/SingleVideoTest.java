package com.monsterdam.multimedia.domain;

import static com.monsterdam.multimedia.domain.ContentPackageTestSamples.*;
import static com.monsterdam.multimedia.domain.SingleVideoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.multimedia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SingleVideoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleVideo.class);
        SingleVideo singleVideo1 = getSingleVideoSample1();
        SingleVideo singleVideo2 = new SingleVideo();
        assertThat(singleVideo1).isNotEqualTo(singleVideo2);

        singleVideo2.setId(singleVideo1.getId());
        assertThat(singleVideo1).isEqualTo(singleVideo2);

        singleVideo2 = getSingleVideoSample2();
        assertThat(singleVideo1).isNotEqualTo(singleVideo2);
    }

    @Test
    void belongPackageTest() throws Exception {
        SingleVideo singleVideo = getSingleVideoRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        singleVideo.setBelongPackage(contentPackageBack);
        assertThat(singleVideo.getBelongPackage()).isEqualTo(contentPackageBack);

        singleVideo.belongPackage(null);
        assertThat(singleVideo.getBelongPackage()).isNull();
    }
}
