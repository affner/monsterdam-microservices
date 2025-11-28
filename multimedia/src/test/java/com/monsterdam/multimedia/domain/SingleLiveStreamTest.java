package com.monsterdam.multimedia.domain;

import static com.monsterdam.multimedia.domain.ContentPackageTestSamples.getContentPackageRandomSampleGenerator;
import static com.monsterdam.multimedia.domain.SingleLiveStreamTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.multimedia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SingleLiveStreamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleLiveStream.class);
        SingleLiveStream singleLiveStream1 = getSingleLiveStreamSample1();
        SingleLiveStream singleLiveStream2 = new SingleLiveStream();
        assertThat(singleLiveStream1).isNotEqualTo(singleLiveStream2);

        singleLiveStream2.setId(singleLiveStream1.getId());
        assertThat(singleLiveStream1).isEqualTo(singleLiveStream2);

        singleLiveStream2 = getSingleLiveStreamSample2();
        assertThat(singleLiveStream1).isNotEqualTo(singleLiveStream2);
    }

    @Test
    void contentPackageTest() throws Exception {
        SingleLiveStream singleLiveStream = getSingleLiveStreamRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        singleLiveStream.setContentPackage(contentPackageBack);
        assertThat(singleLiveStream.getContentPackage()).isEqualTo(contentPackageBack);
        assertThat(contentPackageBack.getAudio()).isEqualTo(singleLiveStream);

        singleLiveStream.contentPackage(null);
        assertThat(singleLiveStream.getContentPackage()).isNull();
        assertThat(contentPackageBack.getAudio()).isNull();
    }
}
