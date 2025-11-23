package com.fanflip.multimedia.domain;

import static com.fanflip.multimedia.domain.ContentPackageTestSamples.*;
import static com.fanflip.multimedia.domain.SingleAudioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.multimedia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SingleAudioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleAudio.class);
        SingleAudio singleAudio1 = getSingleAudioSample1();
        SingleAudio singleAudio2 = new SingleAudio();
        assertThat(singleAudio1).isNotEqualTo(singleAudio2);

        singleAudio2.setId(singleAudio1.getId());
        assertThat(singleAudio1).isEqualTo(singleAudio2);

        singleAudio2 = getSingleAudioSample2();
        assertThat(singleAudio1).isNotEqualTo(singleAudio2);
    }

    @Test
    void contentPackageTest() throws Exception {
        SingleAudio singleAudio = getSingleAudioRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        singleAudio.setContentPackage(contentPackageBack);
        assertThat(singleAudio.getContentPackage()).isEqualTo(contentPackageBack);
        assertThat(contentPackageBack.getAudio()).isEqualTo(singleAudio);

        singleAudio.contentPackage(null);
        assertThat(singleAudio.getContentPackage()).isNull();
        assertThat(contentPackageBack.getAudio()).isNull();
    }
}
